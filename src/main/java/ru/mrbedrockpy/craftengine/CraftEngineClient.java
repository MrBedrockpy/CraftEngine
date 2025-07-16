package ru.mrbedrockpy.craftengine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import ru.mrbedrockpy.craftengine.graphics.Cuboid;
import ru.mrbedrockpy.craftengine.graphics.Mesh;
import ru.mrbedrockpy.craftengine.window.*;
import ru.mrbedrockpy.craftengine.world.ClientWorld;
import ru.mrbedrockpy.craftengine.world.WorldRenderer;
import ru.mrbedrockpy.craftengine.world.entity.ClientPlayerEntity;

public class CraftEngineClient {
    public static CraftEngineClient INSTANCE = new CraftEngineClient();

    private Mouse mouse;
    private CraftEngineClient(){}
    private final FPSCounter fpsCounter = new FPSCounter();
    private ClientWorld clientWorld;
    private ClientPlayerEntity player;

    public void run() {
        Window.initialize(new WindowSettings(1920, 1080, "CraftEngine Client", true, false));
        mouse = new Mouse(Window.getWindow());
        player = new ClientPlayerEntity(new Vector3f(0, 2, 0), mouse);
        clientWorld = new ClientWorld(player);
        clientWorld.generateWorld();
        Input.initialize();
        long lastTime = System.nanoTime();
        while(!Window.isShouldClose()) {
            Input.pullEvents();
            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;
            this.tick(deltaTime);
            Window.clear();
            this.render();
            Window.swapBuffers();
        }
        Window.terminate();
    }

    private void tick(float deltaTime) {
        fpsCounter.update();
        if(Input.jpressed(GLFW.GLFW_KEY_ESCAPE)) {
            Window.setShouldClose(true);
        }
        if(Input.pressed(GLFW.GLFW_KEY_F11)) {
            Window.toggleFullscreen();
        }
        player.update(deltaTime, clientWorld);
    }

    private void render() {
        clientWorld.render();
    }

    public int getFPS() {
        return fpsCounter.getFPS();
    }

}
