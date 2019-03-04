package com.bwh.lwjglengine.engine;

import com.bwh.lwjglengine.graphics.Transformation;
import com.bwh.lwjglengine.graphics.ViewTransformation;
import org.joml.Matrix4f;

public class Camera {
    private final Transformation transformation = new ViewTransformation();

    public Transformation getTransformation() {
        return transformation;
    }

    public Matrix4f getViewMatrix() {
        return transformation.getMatrix();
    }
}
