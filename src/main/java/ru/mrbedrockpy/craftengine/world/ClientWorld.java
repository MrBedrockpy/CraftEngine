package ru.mrbedrockpy.craftengine.world;

import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.graphics.Cuboid;
import ru.mrbedrockpy.craftengine.window.Camera;
import ru.mrbedrockpy.craftengine.world.block.Block;
import ru.mrbedrockpy.craftengine.world.entity.ClientPlayerEntity;
import ru.mrbedrockpy.craftengine.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class ClientWorld extends World {
    private final WorldRenderer worldRenderer;
    private final ClientPlayerEntity player;
    public ClientWorld(ClientPlayerEntity player, TickSystem ticker) {
        this.player = player;
        this.worldRenderer = new WorldRenderer(player.getCamera(), width, height, depth);
        ticker.addListener(this::tick);
    }
    public void render() {
        worldRenderer.render(this, player);
    }
    @Override
    public void generateWorld() {
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                setBlock(x, 0, z, new Block(true));
            }
        }
    }
}
