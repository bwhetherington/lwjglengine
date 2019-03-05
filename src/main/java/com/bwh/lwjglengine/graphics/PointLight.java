package com.bwh.lwjglengine.graphics;

import org.joml.Vector3f;

public class PointLight {
    public Vector3f color;
    public Vector3f position;
    public float intensity;
    public Attenuation attenuation;

    public PointLight(Vector3f color, Vector3f position, float intensity) {
        this(color, position, intensity, new Attenuation(1, 0, 0));
    }

    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
        this.color = color;
        this.position = position;
        this.intensity = intensity;
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) {
        this(pointLight.color, pointLight.position, pointLight.intensity, pointLight.attenuation);
    }

    public static class Attenuation {
        public float constant;
        public float linear;
        public float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }
    }
}
