package com.bwh.lwjglengine.graphics;

import com.bwh.lwjglengine.engine.Camera;
import com.bwh.lwjglengine.engine.Entity;
import com.bwh.lwjglengine.graphics.ShaderProgram;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    private ShaderProgram program;
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

        this.program = program;
        program.bind();
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
        entity.render(program);
    }
}
