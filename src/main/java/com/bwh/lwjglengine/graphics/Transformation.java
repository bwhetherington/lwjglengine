package com.bwh.lwjglengine.graphics;

import com.bwh.lwjglengine.engine.Camera;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Transformation {
    protected final Matrix4f matrix = new Matrix4f().identity();

    protected final Vector3f scale = new Vector3f(1, 1, 1);
    protected final Vector3f translation = new Vector3f(0, 0, 0);
    protected float rotateX, rotateY, rotateZ;

    private boolean shouldUpdate = true;

    protected void calculateMatrix() {
        matrix
                .identity()
                .translate(translation)
                .rotateX(rotateX)
                .rotateY(rotateY)
                .rotateZ(rotateZ)
                .scale(scale);
    }

    public Matrix4f getMatrix() {
        if (shouldUpdate) {
            calculateMatrix();
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

    public Vector3fc getPosition() {
        return translation;
    }

    public float getX() {
        return translation.x;
    }

    public float getY() {
        return translation.y;
    }

    public float getZ() {
        return translation.z;
    }

    /**
     * Produces the scale of the transformation as an immutable 3d vector, such
     * that if (x, y, z) = scale, x represents the x-scale of the
     * transformation, y the y-scale, and z the z-scale.
     * @return the scale of the transformation
     */
    public Vector3fc getScale() {
        return scale;
    }

    /**
     * Produces the pitch of the transformation. Pitch is defined as a rotation
     * around the x-axis.
     * @return the pitch of the transformation
     */
    public float getPitch() {
        return rotateX;
    }

    /**
     * Produces the roll of the transformation. Roll is defined as a rotation
     * around the z-axis.
     * @return the roll of the transformation
     */
    public float getRoll() {
        return rotateZ;
    }

    /**
     * Produces the yaw of the transformation. Yaw is defined as a rotation
     * around the y-axis.
     * @return the yaw of the transformation
     */
    public float getYaw() {
        return rotateY;
    }
}
