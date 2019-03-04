package com.bwh.lwjglengine;

import com.bwh.lwjglengine.engine.Camera;
import com.bwh.lwjglengine.engine.Entity;
import com.bwh.lwjglengine.engine.GameEngine;
import com.bwh.lwjglengine.graphics.*;
import com.bwh.lwjglengine.engine.Window;
import com.bwh.lwjglengine.models.ObjLoader;
import com.bwh.lwjglengine.models.ObjLoader2;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Main extends GameEngine {
    private Renderer renderer;
    private ShaderProgram program;
    private Camera camera = new Camera();
    private List<Entity> entities = new ArrayList<>();
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

            program.createUniform("projectionMatrix");
            program.createUniform("modelMatrix");
            program.createUniform("texture_sampler");

            projection = new Matrix4f()
                    .perspective((float) (Math.PI / 4), (float) getAspectRatio(), 0.01f, 100.0f);

            renderer = new Renderer();
            renderer.init();

            renderer.setProgram(program);


            // Create the Mesh
            float[] positions = new float[] {
                    -0.5f, 0.5f, 0.5f, 1,
                    -0.5f, -0.5f, 0.5f, 1,
                    0.5f, -0.5f, 0.5f, 1,
                    0.5f, 0.5f, 0.5f, 1,
                    -0.5f, 0.5f, -0.5f, 1,
                    0.5f, 0.5f, -0.5f, 1,
                    -0.5f, -0.5f, -0.5f, 1,
                    0.5f, -0.5f, -0.5f, 1,
                    -0.5f, 0.5f, -0.5f, 1,
                    0.5f, 0.5f, -0.5f, 1,
                    -0.5f, 0.5f, 0.5f, 1,
                    0.5f, 0.5f, 0.5f, 1,
                    0.5f, 0.5f, 0.5f, 1,
                    0.5f, -0.5f, 0.5f, 1,
                    -0.5f, 0.5f, 0.5f, 1,
                    -0.5f, -0.5f, 0.5f, 1,
                    -0.5f, -0.5f, -0.5f, 1,
                    0.5f, -0.5f, -0.5f, 1,
                    -0.5f, -0.5f, 0.5f, 1,
                    0.5f, -0.5f, 0.5f, 1
            };
            float[] textCoords = new float[] {
                    0.0f, 0.0f,
                    0.0f, 0.5f,
                    0.5f, 0.5f,
                    0.5f, 0.0f,
                    0.0f, 0.0f,
                    0.5f, 0.0f,
                    0.0f, 0.5f,
                    0.5f, 0.5f,
                    0.0f, 0.5f,
                    0.5f, 0.5f,
                    0.0f, 1.0f,
                    0.5f, 1.0f,
                    0.0f, 0.0f,
                    0.0f, 0.5f,
                    0.5f, 0.0f,
                    0.5f, 0.5f,
                    0.5f, 0.0f,
                    1.0f, 0.0f,
                    0.5f, 0.5f,
                    1.0f, 0.5f
            };
            int[] indices = new int[] {
                    0, 1, 3, 3, 1, 2,
                    8, 10, 11, 9, 8, 11,
                    12, 13, 7, 5, 12, 7,
                    14, 15, 6, 4, 14, 6,
                    16, 18, 19, 17, 16, 19,
                    4, 6, 7, 5, 4, 7
            };
            Texture texture = new Texture("textures/grassblock.png");
            program.setUniform("texture_sampler", 0);

//            Mesh mesh = ObjLoader.loadObj("models/cube.obj").toMesh();
            Mesh mesh = ObjLoader2.loadMesh("models/cube.obj");
            mesh.setTexture(texture);

            for (int i = -10; i <= 10; i++) {
                for (int j = -10; j <= 10; j++) {
                    Entity e = new Entity(mesh);
                    e.getTransformation().translate(i * 2, j * 2, 0);
                    entities.add(e);
                }
            }

            program.setUniform("projection", projection);

            camera.getTransformation()
                    .translate(0, 0, 50);

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
            projection.perspective((float) (Math.PI / 2), (float) getAspectRatio(), 0.01f, 100.0f);
            window.setResized(false);
        }
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        for (Entity e : entities) {
            renderer.render(camera, e);
        }
    }

    @Override
    protected void input(Window window) {

    }

    public static void main(String[] args) {
        final GameEngine main = new Main();
        main.start();
    }
}
