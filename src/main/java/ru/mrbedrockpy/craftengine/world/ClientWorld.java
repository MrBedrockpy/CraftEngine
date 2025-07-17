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
    private final List<LivingEntity> entities = new ArrayList<>();

    public ClientWorld(Camera camera, TickSystem tiker) {
        this.worldRenderer = new WorldRenderer(camera, width, height, depth);
        tiker.addListener(this::tick);
    }

    public void tick(){
        for (LivingEntity entity : entities) {
            entity.tick();
        }
    }
    @Override
    public void render() {
        worldRenderer.render(this);
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
