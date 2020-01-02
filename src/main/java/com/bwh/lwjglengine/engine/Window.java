package com.bwh.lwjglengine.engine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private long windowId;
    private boolean isVSync;
    private boolean isResized;
    private int width, height;

    public Window(final String name, final int width, final int height, boolean isVSync) {
        init(name, width, height, isVSync);
    }

    private void init(final String name, final int width, final int height, boolean isVSync) {
        this.isVSync = isVSync;
        this.width = width;
        this.height = height;

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        windowId = glfwCreateWindow(width, height, name, NULL, NULL);
        if (windowId == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwSetKeyCallback(windowId, (window, key, scanCode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(windowId, true);
            }
        });

        glfwSetFramebufferSizeCallback(windowId, (window, newWidth, newHeight) -> {
            Window.this.width = newWidth;
            Window.this.height = newHeight;
            isResized = true;
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(windowId, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(windowId, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);
        }

        show();
    }

    public void show() {
        glfwShowWindow(windowId);
    }

    public void hide() {
        glfwHideWindow(windowId);
    }

    private void loop() {
        GL.createCapabilities();

        glClearColor(0, 0, 0, 1);

        while (!glfwWindowShouldClose(windowId)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwSwapBuffers(windowId);

            glfwPollEvents();
        }
    }

    public boolean isVSync() {
        return isVSync;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(windowId);
    }

    public void initGL() {
        glfwMakeContextCurrent(windowId);
        GL.createCapabilities();
        if (isVSync) {
            glfwSwapInterval(1);
        }
    }

    public void update() {
        glfwSwapBuffers(windowId);
        glfwPollEvents();
    }

    public boolean isResized() {
        return isResized;
    }

    public void setResized(boolean isResized) {
        this.isResized = isResized;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void hideCursor() {
        glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public void showCursor() {
        glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public void setMouseListener(MouseListener listener) {
        glfwSetCursorPosCallback(windowId, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                listener.onMouseMove(x, y);
            }
        });
    }
}
