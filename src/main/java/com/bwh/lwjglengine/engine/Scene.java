package com.bwh.lwjglengine.engine;

import com.bwh.lwjglengine.graphics.Renderer;
import com.bwh.lwjglengine.graphics.ShaderProgram;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Skybox skybox;
    private List<Entity> entities;
    private Matrix4f modelViewMatrix = new Matrix4f();

    public Scene() {
        entities = new ArrayList<>();

        // Create skybox shader
    }

    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void render(Renderer renderer, Camera camera) {
        // Render skybox
        renderer.renderSkybox(camera, skybox);

        // Render entities
        for (Entity e : entities) {
            renderer.render(camera, e);
        }
    }
}
