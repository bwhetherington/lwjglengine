package com.bwh.lwjglengine.graphics;

public class ViewTransformation extends Transformation {
    @Override
    public Transformation translate(float x, float y, float z) {
        super.translate(-x, -y, -z);
        return this;
    }

    @Override
    protected void calculateMatrix() {
        matrix
                .identity()
                .rotateX(rotateX)
                .rotateY(rotateY)
                .rotateZ(rotateZ)
                .translate(translation);
    }
}
