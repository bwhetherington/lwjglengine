package com.bwh.lwjglengine.graphics;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transformation {
    private Matrix4f matrix = new Matrix4f().identity();

    private Vector3f scale = new Vector3f(1, 1, 1);
    private Vector3f translation = new Vector3f(0, 0, 0);
    private float rotateX, rotateY, rotateZ;
//    private Quaternionf rotation = new Quaternionf().identity();

    private boolean shouldUpdate = true;

    public Matrix4f getMatrix() {
        if (shouldUpdate) {
            matrix
                    .identity()
                    .translate(translation)
                    .rotateX(rotateX)
                    .rotateY(rotateY)
                    .rotateZ(rotateZ)
                    .scale(scale);
            unflag();
        }
        return matrix;
    }

    private void flag() {
        shouldUpdate = true;
    }

    private void unflag() {
        shouldUpdate = false;
    }

    public Transformation translate(float x, float y, float z) {
        translation.set(x, y, z);
        flag();
        return this;
    }

    public Transformation scale(float x, float y, float z) {
        scale.set(x, y, z);
        flag();
        return this;
    }

    public Transformation rotateX(float theta) {
//        rotation.identity()
//                .rotateX(theta);
        rotateX = theta;
        flag();
        return this;
    }

    public Transformation rotateY(float theta) {
        rotateY = theta;
        flag();
        return this;
    }

    public Transformation rotateZ(float theta) {
        rotateZ = theta;
        flag();
        return this;
    }
}
