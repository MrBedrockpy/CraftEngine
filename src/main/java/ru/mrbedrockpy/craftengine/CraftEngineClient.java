package ru.mrbedrockpy.craftengine;

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
    private Mouse mouse;
    public static final EventManager eventManager = new EventManager();

    private CraftEngineClient(){}
    private final FPSCounter fpsCounter = new FPSCounter();
    private ClientWorld clientWorld;
    private ClientPlayerEntity player;
    private final TickSystem tickSystem = new TickSystem(20);


    public void run() {
        Window.initialize(new WindowSettings(1920, 1080, "CraftEngine Client", true, false));
        Input.initialize();
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
        mouse = new Mouse(Window.getWindow());
        player = new ClientPlayerEntity(new Vector3f(5, 10, 5), mouse, clientWorld);
        clientWorld = new ClientWorld(player.getCamera(), tickSystem);
        player.setWorld(clientWorld);
        clientWorld.generateWorld();
        eventManager.addListener(MouseClickEvent.class, event -> {
            player.onMouseClick(event.getButton(), event.getX(), event.getY());
        });
        context = new DrawContext(Window.getWidth(), Window.getHeight());
        hudRenderer = new HudRenderer(Window.getWidth(), Window.getHeight());
        hudRenderer.texture = Texture.load("cursor.png");
    }

    private void update(float deltaTime) {
        fpsCounter.update();
        tickSystem.update(deltaTime);
        if(Input.jpressed(GLFW.GLFW_KEY_ESCAPE)) {
            Window.setShouldClose(true);
        }
        if(Input.pressed(GLFW.GLFW_KEY_F11)) {
            Window.toggleFullscreen();
        }
        if (mouse.isButtonClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            double[] xpos = new double[1], ypos = new double[1];
            GLFW.glfwGetCursorPos(Window.getWindow(), xpos, ypos);
            MouseClickEvent clickEvent = new MouseClickEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT, xpos[0], ypos[0]);
            eventManager.callEvent(clickEvent);
        }
        player.update(deltaTime, clientWorld);
    }

    private void render() {
        clientWorld.render();
        hudRenderer.render(context);
    }

    public int getFPS() {
        return fpsCounter.getFPS();
    }

}
