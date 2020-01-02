package com.bwh.lwjglengine.graphics;

import com.bwh.lwjglengine.engine.Camera;
import com.bwh.lwjglengine.engine.Entity;
import com.bwh.lwjglengine.engine.Skybox;
import com.bwh.lwjglengine.graphics.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    private ShaderProgram program;
    private ShaderProgram skyboxProgram;
    private Matrix4f modelViewMatrix = new Matrix4f().identity();

    public void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public void setProgram(ShaderProgram program) {
        // Unbind previous program
        if (this.program != null) {
            this.program.unbind();
        }

        setupSkyboxProgram();

        this.program = program;
        program.bind();
    }

    public void setSkyboxProgram(ShaderProgram program) {
        this.skyboxProgram = program;
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Camera camera, Entity entity) {
        Matrix4f view = camera.getViewMatrix();
        modelViewMatrix
                .identity()
                .mul(view)
                .mul(entity.getTransformation().getMatrix());
        program.setUniform("modelView", modelViewMatrix);
        entity.render(this);
    }

    public void renderSkybox(Camera camera, Skybox skybox) {
        Matrix4f view = camera.getViewMatrix();
        modelViewMatrix
                .identity()
                .mul(view)
                .m30(0)
                .m31(0)
                .m32(0)
                .mul(skybox.getTransformation().getMatrix());
        program.unbind();
        skyboxProgram.bind();
        skyboxProgram.setUniform("modelView", modelViewMatrix);

        // Cull front faces
//        glCullFace(GL_FRONT);
        skybox.render(this);
        skyboxProgram.unbind();
        program.bind();
        // Cull back faces
//        glCullFace(GL_BACK);
    }

    public ShaderProgram getProgram() {
        return program;
    }

    public void setupSkyboxProgram() {
        skyboxProgram = new ShaderProgram("shaders/skybox.vert", "shaders/skybox.frag");
        skyboxProgram.bind();

        skyboxProgram.createUniform("projection");
        skyboxProgram.createUniform("modelView");
        skyboxProgram.createUniform("texture_sampler");
        skyboxProgram.createUniform("ambientLight");

        skyboxProgram.setUniform("texture_sampler", 0);
        skyboxProgram.setUniform("ambientLight", new Vector3f(1, 1, 1));
        skyboxProgram.unbind();
    }

    public void setProjection(Matrix4f projection) {
        program.bind();
        program.setUniform("projection", projection);
        program.unbind();

        skyboxProgram.bind();
        skyboxProgram.setUniform("projection", projection);
        skyboxProgram.unbind();

        program.bind();
    }
}
