package ru.mrbedrockpy.craftengine;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import ru.mrbedrockpy.craftengine.graphics.Mesh;
import ru.mrbedrockpy.craftengine.window.*;

public class CraftEngineClient {
    public static CraftEngineClient INSTANCE = new CraftEngineClient();

    private Camera camera = new Camera();
    private CraftEngineClient(){}
    private final FPSCounter fpsCounter = new FPSCounter();
    private Mesh mesh;

    public void run() {
        Window.initialize(new WindowSettings(800, 600, "CraftEngine Client", true, false));
        Input.initialize();
        mesh = new Mesh(vertices);
        while(!Window.isShouldClose()) {
            Input.pullEvents();
            this.tick();
            Window.clear();
            this.render();
            Window.swapBuffers();
        }
        Window.terminate();
    }

    private void tick() {
        fpsCounter.update();
        if(Input.jpressed(GLFW.GLFW_KEY_ESCAPE)) {
            Window.setShouldClose(true);
        }
        if(Input.jpressed(GLFW.GLFW_KEY_F11)) {
            Window.toggleFullscreen();
        }
        if(Input.jpressed(GLFW.GLFW_KEY_DOWN)) {
            camera.rotate(new Vector2f(0.1f, 0.0f));
        }

    }

    float[] vertices = {
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
    };
    private void render() {

        mesh.render(camera);
    }

    public int getFPS() {
        return fpsCounter.getFPS();
    }

}
