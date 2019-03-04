package com.bwh.lwjglengine.graphics;

import org.lwjgl.system.MemoryStack;

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

    private Texture texture;

    public Mesh(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
        System.out.println("v:  " + vertices.length / 3);
        System.out.println("vt: " + texCoords.length / 2);
        System.out.println("vn: " + normals.length / 3);
        System.out.println("p:  " + indices.length);

        vertexCount = indices.length;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Vertices
            FloatBuffer verticesBuffer = stack.mallocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            posVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates
            FloatBuffer texBuffer = stack.mallocFloat(texCoords.length);
            texBuffer.put(texCoords).flip();

            texVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, texVboId);
            glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Normals
            FloatBuffer normBuffer = stack.mallocFloat(normals.length);
            normBuffer.put(normals).flip();

            normVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, normVboId);
            glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            // Indices
            IntBuffer indicesBuffer = stack.mallocInt(indices.length);
            indicesBuffer.put(indices).flip();

            idxVboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        } finally {
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void render(ShaderProgram program) {
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        if (texture != null) {
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        glBindTexture(GL_TEXTURE_2D, 0);

        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    public void cleanup() {

    }
}
