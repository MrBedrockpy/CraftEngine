package ru.mrbedrockpy.craftengine.world;

import org.joml.Vector2i;
import ru.mrbedrockpy.craftengine.world.block.Block;
import ru.mrbedrockpy.craftengine.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    public static final int WIDTH = 32;
    public static final int HEIGHT = 16;

    private final Vector2i position;
    private final Block[][][] blocks;
    private final List<LivingEntity> entities = new ArrayList<>();

    public Chunk(Vector2i position) {
        this.position = position;
        this.blocks = new Block[WIDTH][HEIGHT][WIDTH];
    }

    public Chunk(Vector2i position, Block[][][] blocks) {
        this.position = position;
        this.blocks = blocks;
    }

    public Block getBlock(int x, int y, int z) {
        try {
            return blocks[x][y][z];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean setBlock(int x, int y, int z, Block block) {
        try {
           blocks[x][y][z] = block;
           return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public void tick() {
        for (LivingEntity entity : entities) {
            entity.tick();
        }
    }

    public Vector2i getPosition() {
        return new Vector2i(position);
    }

    public void setEntities(List<LivingEntity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
    }

    public List<LivingEntity> getEntities() {
        return new ArrayList<>(entities);
    }
}
