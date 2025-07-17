package ru.mrbedrockpy.craftengine.world;

import lombok.Getter;
import org.joml.Vector3f;
import org.joml.Vector3i;
import ru.mrbedrockpy.craftengine.world.block.Block;
import ru.mrbedrockpy.craftengine.world.entity.LivingEntity;
import ru.mrbedrockpy.craftengine.world.raycast.BlockRaycastResult;

import java.util.ArrayList;
import java.util.List;

public abstract class World {
    protected final int width = 32;
    protected final int height = 16;
    protected final int depth = 32;
    @Getter
    protected final List<LivingEntity> entities = new ArrayList<>();
    protected Block[][][] blocks = new Block[width][height][depth];
    public void tick() {
        for (LivingEntity entity : entities) {
            entity.tick();
        }
    }

    public void addEntity(LivingEntity entity) {
        entities.add(entity);
    }

    public abstract void render();
    public abstract void generateWorld();
    public Block getBlock(int x, int y, int z) {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth) return null;
        return blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth) return;
        blocks[x][y][z] = block;
    }

    public BlockRaycastResult raycast(Vector3f origin, Vector3f direction, float maxDistance) {
        Vector3f pos = new Vector3f(origin);
        Vector3i blockPos = new Vector3i(
                (int) Math.floor(pos.x),
                (int) Math.floor(pos.y),
                (int) Math.floor(pos.z)
        );

        float deltaDistX = direction.x == 0 ? Float.POSITIVE_INFINITY : Math.abs(1f / direction.x);
        float deltaDistY = direction.y == 0 ? Float.POSITIVE_INFINITY : Math.abs(1f / direction.y);
        float deltaDistZ = direction.z == 0 ? Float.POSITIVE_INFINITY : Math.abs(1f / direction.z);

        int stepX = direction.x > 0 ? 1 : (direction.x < 0 ? -1 : 0);
        int stepY = direction.y > 0 ? 1 : (direction.y < 0 ? -1 : 0);
        int stepZ = direction.z > 0 ? 1 : (direction.z < 0 ? -1 : 0);

        float sideDistX = stepX > 0
                ? ((float)(Math.floor(pos.x) + 1.0) - pos.x) * deltaDistX
                : (pos.x - (float)Math.floor(pos.x)) * deltaDistX;
        if (stepX == 0) sideDistX = Float.POSITIVE_INFINITY;

        float sideDistY = stepY > 0
                ? ((float)(Math.floor(pos.y) + 1.0) - pos.y) * deltaDistY
                : (pos.y - (float)Math.floor(pos.y)) * deltaDistY;
        if (stepY == 0) sideDistY = Float.POSITIVE_INFINITY;

        float sideDistZ = stepZ > 0
                ? ((float)(Math.floor(pos.z) + 1.0) - pos.z) * deltaDistZ
                : (pos.z - (float)Math.floor(pos.z)) * deltaDistZ;
        if (stepZ == 0) sideDistZ = Float.POSITIVE_INFINITY;

        float distance = 0f;

        while (distance <= maxDistance) {
            Block block = getBlock(blockPos.x, blockPos.y, blockPos.z);
            if (block != null && block.isSolid()) {
                return new BlockRaycastResult(blockPos.x, blockPos.y, blockPos.z, block);
            }

            if (sideDistX < sideDistY) {
                if (sideDistX < sideDistZ) {
                    blockPos.x += stepX;
                    distance = sideDistX;
                    sideDistX += deltaDistX;
                } else {
                    blockPos.z += stepZ;
                    distance = sideDistZ;
                    sideDistZ += deltaDistZ;
                }
            } else {
                if (sideDistY < sideDistZ) {
                    blockPos.y += stepY;
                    distance = sideDistY;
                    sideDistY += deltaDistY;
                } else {
                    blockPos.z += stepZ;
                    distance = sideDistZ;
                    sideDistZ += deltaDistZ;
                }
            }
        }

        return null;
    }
}