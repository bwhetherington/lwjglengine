package com.bwh.lwjglengine.engine;

import com.bwh.lwjglengine.graphics.Mesh;
import com.bwh.lwjglengine.graphics.Renderer;
import com.bwh.lwjglengine.graphics.ShaderProgram;
import com.bwh.lwjglengine.graphics.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3fc;

public class Entity {
    private Mesh mesh;
    private Transformation transformation = new Transformation();

    public Entity(Mesh mesh) {
        this.mesh = mesh;
    }

    public void render(Renderer renderer) {
        mesh.render(renderer.getProgram());
    }

    /**
     * Produces the transformation used by the entity.
     * @return the transformation of the entity
     */
    public Transformation getTransformation() {
        return transformation;
    }

    /**
     * Sets the mesh to be used by the entity to the specified mesh.
     * @param mesh the mesh
     */
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    /**
     * Sets the position of the entity to the point specified by the specified
     * x, y, and z-coordinates.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    public void setPosition(float x, float y, float z) {
        getTransformation().translate(x, y, z);
    }

    /**
     * Produces the position of the entity as an immutable 3 dimensional
     * vector.
     * @return the position of the entity
     */
    public Vector3fc getPosition() {
        return getTransformation().getPosition();
    }
}
