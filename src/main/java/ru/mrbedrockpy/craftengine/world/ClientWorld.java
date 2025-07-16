package ru.mrbedrockpy.craftengine.world;

import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.graphics.Cuboid;
import ru.mrbedrockpy.craftengine.window.Camera;
import ru.mrbedrockpy.craftengine.world.entity.ClientPlayerEntity;

import java.util.List;

public class ClientWorld {
    private WorldRenderer worldRenderer;
    private Camera camera;
    private final ClientPlayerEntity player;
    public ClientWorld(ClientPlayerEntity player) {
        this.camera = player.getCamera();
        this.worldRenderer = new WorldRenderer(camera);
        this.player = player;
    }

    public void render() {
        worldRenderer.render();
    }

    public void generateWorld() {
        for(int x = -10; x < 10; x++) {
            for (int z = -10; z < 10; z++) {
                Cuboid cuboid = new Cuboid(new Vector3f(x, 0, z), new Vector3f(1, 1, 1));
                worldRenderer.addCuboid(cuboid);
            }
        }
    }
}
