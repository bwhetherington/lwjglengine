package com.bwh.lwjglengine.graphics;

import org.joml.Vector4f;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Material {
    private static final Vector4f DEFAULT_COLOR = new Vector4f(1, 1, 1, 1);

    public Vector4f ambient;
    public Vector4f diffuse;
    public Vector4f specular;
    public float reflectance;
    public Texture texture;

    public Material(Vector4f ambient, Vector4f diffuse, Vector4f specular, Texture texture, float reflectance) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public Material() {
        this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, null, 0);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, texture, reflectance);
    }

    public boolean hasTexture() {
        return texture != null;
    }

    public void bind(ShaderProgram program) {
        program.setUniform("material", this);
        if (hasTexture()) {
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
        }
    }

    public void unbind() {
        if (hasTexture()) {
            texture.unbind();
        }
    }
}
