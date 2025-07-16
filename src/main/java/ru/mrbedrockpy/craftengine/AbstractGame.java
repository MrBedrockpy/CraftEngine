package ru.mrbedrockpy.craftengine;

import lombok.Getter;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;
import ru.mrbedrockpy.craftengine.player.Player;
import ru.mrbedrockpy.craftengine.window.Input;
import ru.mrbedrockpy.craftengine.window.Window;
import ru.mrbedrockpy.craftengine.window.WindowSettings;
import ru.mrbedrockpy.craftengine.world.World;
import ru.mrbedrockpy.craftengine.world.block.Block;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

@Getter
public abstract class AbstractGame {

    private final WindowSettings windowSettings;

    private long tick = 0;
    private float delta = 0;

    private Player player;
    private World world;
    private Block block;

    public AbstractGame(WindowSettings windowSettings) {
        this.windowSettings = windowSettings;
    }

    public final void run() {
        initialize();
        while (!Window.isShouldClose()) {
            setupTime();
            tick();
            Window.clear();
            render();
            Window.swapBuffers();
            Input.pullEvents();
        }
        terminate();
    }

    protected void setupTime() {
        long newTick = Math.round(glfwGetTime());
        delta = newTick - tick;
        tick = newTick;
    }

    protected void initialize() {
        Window.initialize(windowSettings);
        Input.initialize();
        block = new Block(new Vector3i(1, 1, -1));
    }

    protected void tick() {
        if (Input.jpressed(GLFW_KEY_ESCAPE)) Window.setShouldClose(true);
    }

    protected void render() {
        block.render();
    }

    protected void terminate() {
        Window.terminate();
    }
}
