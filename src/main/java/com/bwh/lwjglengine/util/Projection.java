package com.bwh.lwjglengine.util;

import org.joml.Matrix4f;

public class Projection {
    public static final float FOV_Y = (float) Math.PI / 4;
    public static final float NEAR_Z = 0.01f;
    public static final float FAR_Z = 100.0f;

    public static Matrix4f getProjection(float aspect) {
        return new Matrix4f().identity()
                .perspective(FOV_Y, aspect, NEAR_Z, FAR_Z);
    }
}
