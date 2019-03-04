package com.bwh.lwjglengine.engine;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class GameEngine implements Runnable {
    private Thread gameLoopThread;
    private static final double UPDATE_DURATION = 1.0 / 60.0;
    private static final double FRAME_DURATION = 1.0 / 60.0;
    private Timer timer = new Timer();
    private Window window;

    public GameEngine(String name, int width, int height) {
        window = new Window(name, width, height, true);
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
    }

    @Override
    public void run() {
        timer.init();
        window.initGL();
        init();
        loop();
    }

    public void init() {}

    public void start() {
        String osName = System.getProperty("os.name");
//        if (osName.contains("Mac")) {
//            gameLoopThread.run();
//        } else {
//            gameLoopThread.start();
//        }
        run();
    }

    public void loop() {
        double elapsedTime;
        double accumulator = 0;
        double interval = UPDATE_DURATION;

        while (!window.shouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input(window);

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (window.isVSync()) {
                sync();
            }

            window.update();
        }
    }

    private void sync() {
        double endTime = timer.getLastLoopTime() + FRAME_DURATION;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void input(Window window) {}

    protected void render() {}

    protected void update(double interval) {}

    public Window getWindow() {
        return window;
    }
}
