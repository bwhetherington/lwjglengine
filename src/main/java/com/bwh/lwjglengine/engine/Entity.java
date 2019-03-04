package com.bwh.lwjglengine.engine;

import com.bwh.lwjglengine.graphics.Mesh;
import com.bwh.lwjglengine.graphics.ShaderProgram;
import com.bwh.lwjglengine.graphics.Transformation;
import org.joml.Matrix4f;

public class Entity {
    private Mesh mesh;
    private Transformation transformation = new Transformation();

    public Entity(Mesh mesh) {
        this.mesh = mesh;
    }

    public void render(ShaderProgram program) {
        mesh.render(program);
    }

    public Transformation getTransformation() {
        return transformation;
    }
}
