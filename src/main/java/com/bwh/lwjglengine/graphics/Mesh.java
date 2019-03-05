package com.bwh.lwjglengine.graphics;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;



public class Mesh {
    private int vaoId;
    private int posVboId;
    private int texVboId;
    private int normVboId;
    private int idxVboId;
    private int vertexCount;

    private Material material = new Material();

    public Mesh(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
        System.out.println("v:  " + vertices.length / 3);
        System.out.println("vt: " + texCoords.length / 2);
        System.out.println("vn: " + normals.length / 3);
        System.out.println("p:  " + indices.length);

        FloatBuffer verticesBuffer = null;
        FloatBuffer texBuffer = null;
        FloatBuffer normBuffer = null;
        IntBuffer indicesBuffer = null;

        vertexCount = indices.length;
        try {
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Vertices
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            posVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates
            texBuffer = MemoryUtil.memAllocFloat(texCoords.length);
            texBuffer.put(texCoords).flip();

            texVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, texVboId);
            glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Normals
            normBuffer = MemoryUtil.memAllocFloat(normals.length);
            normBuffer.put(normals).flip();

            normVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, normVboId);
            glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Indices
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();

            idxVboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        } finally {
            MemoryUtil.memFree(verticesBuffer);
            MemoryUtil.memFree(texBuffer);
            MemoryUtil.memFree(normBuffer);
            MemoryUtil.memFree(indicesBuffer);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void render(ShaderProgram program) {
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        material.bind(program);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);

        program.validate();
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        material.unbind();
        glBindTexture(GL_TEXTURE_2D, 0);

        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }

    public void cleanup() {

    }
}
