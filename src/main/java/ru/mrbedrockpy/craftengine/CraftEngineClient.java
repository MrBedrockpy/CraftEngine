package ru.mrbedrockpy.craftengine;

import lombok.Getter;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import ru.mrbedrockpy.craftengine.event.EventManager;
import ru.mrbedrockpy.craftengine.event.MouseClickEvent;
import ru.mrbedrockpy.craftengine.graphics.Texture;
import ru.mrbedrockpy.craftengine.gui.DrawContext;
import ru.mrbedrockpy.craftengine.gui.HudRenderer;
import ru.mrbedrockpy.craftengine.window.*;
import ru.mrbedrockpy.craftengine.world.ClientWorld;
import ru.mrbedrockpy.craftengine.world.TickSystem;
import ru.mrbedrockpy.craftengine.world.entity.ClientPlayerEntity;

public class CraftEngineClient {
    public static CraftEngineClient INSTANCE = new CraftEngineClient();
    public DrawContext context;
    public HudRenderer hudRenderer;
    public final EventManager eventManager = new EventManager();
    private final FPSCounter fpsCounter = new FPSCounter();
    private ClientWorld clientWorld;
    @Getter
    private ClientPlayerEntity player;
    private final TickSystem tickSystem = new TickSystem(20);

    private CraftEngineClient() {}

    public void run() {
        this.initialize();
        long lastTime = System.nanoTime();
        while(!Window.isShouldClose()) {
            Input.pullEvents();
            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;
            this.update(deltaTime);
            Window.clear();
            this.render();
            Window.swapBuffers();
        }
        Window.terminate();
    }

    public void initialize() {
        Window.initialize(new WindowSettings(1280, 720, "CraftEngine Client", false, true));
        Input.initialize();
        player = new ClientPlayerEntity(new Vector3f(5, 100, 5), clientWorld);
        clientWorld = new ClientWorld(player, tickSystem);
        player.setWorld(clientWorld);
        clientWorld.generateWorld();
        eventManager.addListener(MouseClickEvent.class, player::onMouseClick);
        context = new DrawContext(Window.getWidth(), Window.getHeight());
        hudRenderer = new HudRenderer(Window.getWidth(), Window.getHeight());
        hudRenderer.texture = Texture.load("cursor.png");
        hudRenderer.hudTexture = Texture.load("hotbar.png");
    }

    private void update(float deltaTime) {
        fpsCounter.update();
        tickSystem.update(deltaTime);
        if(Input.jpressed(GLFW.GLFW_KEY_ESCAPE)) {
            Window.setShouldClose(true);
        }
        if (Input.jclicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            MouseClickEvent clickEvent = new MouseClickEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT, Input.getX(), Input.getY());
            eventManager.callEvent(clickEvent);
        }
        player.update(deltaTime, clientWorld);
    }

    private void render() {
        clientWorld.render();
        hudRenderer.render(context);
    }
}
