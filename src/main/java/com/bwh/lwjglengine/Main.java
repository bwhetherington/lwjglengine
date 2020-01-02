package com.bwh.lwjglengine;

import com.bwh.lwjglengine.engine.*;
import com.bwh.lwjglengine.graphics.*;
import com.bwh.lwjglengine.models.ObjLoader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import com.bwh.lwjglengine.Function;

public class Main extends GameEngine {
    private Renderer renderer;
    private ShaderProgram program;
    private Camera camera = new Camera();
    private List<Entity> entities = new ArrayList<>();
    private Scene scene;
    private Matrix4f projection;
    private double theta = 0;

    public Main() {
        super("My Game", 500, 500);
    }

    private double getAspectRatio() {
        Window window = getWindow();
        double width = window.getWidth();
        double height = window.getHeight();
        return width / height;
    }

    @Override
    public void init() {
        try {
            program = new ShaderProgram("shaders/basic.vert", "shaders/basic.frag");

            projection = new Matrix4f()
                    .perspective((float) (Math.PI / 4), (float) getAspectRatio(), 0.01f, 1000.0f);

            renderer = new Renderer();
            renderer.init();

            renderer.setProgram(program);

            program.createUniform("projection");
            program.createUniform("modelView");
            program.createUniform("texture_sampler");
            program.createMaterialUniform("material");
            program.createPointLightUniform("pointLight");
            program.createUniform("ambientLight");
            program.createUniform("specularPower");

            Texture texture = new Texture("textures/grassblock.png");
            program.setUniform("texture_sampler", 0);

            Mesh mesh = ObjLoader.loadMesh("models/cube.obj");

            Material mat = new Material(texture, 1f);

            mesh.setMaterial(mat);

            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    Entity e = new Entity(mesh);
                    e.getTransformation().translate(i * 2, j * 2, 0);
                    entities.add(e);
                }
            }

            renderer.setProjection(projection);

            scene = new Scene();

            Skybox skybox = new Skybox("models/skybox.obj", "textures/skybox.png");
            skybox.getTransformation().scale(100, 100, 100);
            scene.setSkybox(skybox);

            for (Entity e : entities) {
                scene.addEntity(e);
            }

            camera.getTransformation()
                    .translate(0, 0, 15);

            PointLight light = new PointLight(
                    new Vector3f(0.3f, 0.3f, 0.3f),
                    new Vector3f(0, 0, 1),
                    1
            );

            program.setUniform("pointLight", light);
            program.setUniform("ambientLight", new Vector3f(1, 1, 1));
            program.setUniform("specularPower", 1);

            Window window = getWindow();
            window.hideCursor();

            window.setMouseListener(new MouseListener() {
                private double prevX, prevY;
                private boolean hasStarted = false;
                private float pitch = 0;
                private float yaw = 0;

                @Override
                public void onMouseMove(double x, double y) {
                    if (hasStarted) {
                        double dx = x - prevX;
                        double dy = y - prevY;

                        float dYaw = (float) dx * 0.005f;
                        float dPitch = (float) dy * 0.005f;

                        pitch += dPitch;
                        yaw += dYaw;

                        camera.getTransformation()
                                .rotateY(yaw)
                                .rotateX(pitch);
                    }
                    prevX = x;
                    prevY = y;

                    hasStarted = true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void update(double interval) {
        theta += 0.5 * interval;
//        camera.getTransformation()
//                .rotateY((float) theta);
        for (int i = 0; i < entities.size(); i++) {
            final Entity e = entities.get(i);
            final Transformation tx = e.getTransformation();
            final float rotation = (float) theta;
            switch (i % 3) {
                case 0:
                    tx.rotateX(rotation);
                    break;
                case 1:
                    tx.rotateY(rotation);
                    break;
                case 2:
                    tx.rotateZ(rotation);
                    break;
            }
        }
    }

    @Override
    protected void render() {
        Window window = getWindow();
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            projection.identity().perspective((float) (Math.PI / 2), (float) getAspectRatio(), 0.01f, 100.0f);
            renderer.setProjection(projection);
            window.setResized(false);
        }
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        scene.render(renderer, camera);
    }

    @Override
    protected void input(Window window) {

    }

    public static void function(String s, int i, Function f) {
        f.run(s, i);
    }

    public static void main(String[] args) {
        function("Test", 42, (x, y) -> System.out.println(x + y));

//        final GameEngine main = new Main();
//        main.start();
    }
}
