package com.bwh.lwjglengine.graphics;

import com.bwh.lwjglengine.util.FileUtil;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class ShaderProgram {
    private int programId;
    private int vShaderId;
    private int fShaderId;
    private Map<String, Integer> uniforms;

    public ShaderProgram(String vShader, String fShader) {
        uniforms = new HashMap<>();
        programId = glCreateProgram();
        try {
            init(vShader, fShader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(String vShader, String fShader) throws Exception {
        vShaderId = createShader(vShader, GL_VERTEX_SHADER);
        fShaderId = createShader(fShader, GL_FRAGMENT_SHADER);
        link();
    }

    public void createUniform(String name) {
        int location = glGetUniformLocation(programId, name);
        uniforms.put(name, location);
    }

    public int getUniformLocation(String name) {
        return uniforms.getOrDefault(name, 0);
    }

    public void setUniform(String name, Matrix4f mat) {
        int location = glGetUniformLocation(programId, name);
        if (location >= 0) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer buf = stack.mallocFloat(16);
                mat.get(buf);
                glUniformMatrix4fv(location, false, buf);
            }
        } else {
            System.err.println("Unknown uniform: " + name);
        }
    }

    public void setUniform(String name, float val) {
        int location = glGetUniformLocation(programId, name);
        if (location >= 0) {
            glUniform1f(location, val);
        } else {
            System.err.println("Unknown uniform: " + name);
        }
    }

    public void setUniform(String name, int val) {
        int location = glGetUniformLocation(programId, name);
        if (location >= 0) {
            glUniform1i(location, val);
        } else {
            System.err.println("Unknown uniform: " + name);
        }
    }

    private int createShader(String file, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader of type: " + shaderType);
        }

        String source = FileUtil.readFile(file);
        glShaderSource(shaderId, source);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling shader: " + glGetShaderInfoLog(shaderId));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vShaderId != 0) {
            glDetachShader(programId, vShaderId);
        }
        if (fShaderId != 0) {
            glDetachShader(programId, fShaderId);
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
