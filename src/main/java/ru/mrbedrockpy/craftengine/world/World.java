package ru.mrbedrockpy.craftengine.world;

import lombok.Getter;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import ru.mrbedrockpy.craftengine.phys.AABB;
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

    public abstract void generateWorld();
    public Block getBlock(int x, int y, int z) {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth) return null;
        return blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth) return;
        blocks[x][y][z] = block;
    }


    public BlockRaycastResult raycast(Vector3f originF, Vector3f directionF, float maxDistanceF) {

        Vector3d origin = new Vector3d(originF.x, originF.y, originF.z);
        Vector3d direction = new Vector3d(directionF.x, directionF.y, directionF.z);

        Vector3d pos = new Vector3d(origin);
        Vector3i blockPos = new Vector3i(
                (int) Math.floor(pos.x),
                (int) Math.floor(pos.y),
                (int) Math.floor(pos.z)
        );

        double deltaDistX = direction.x == 0 ? Double.POSITIVE_INFINITY : Math.abs(1.0 / direction.x);
        double deltaDistY = direction.y == 0 ? Double.POSITIVE_INFINITY : Math.abs(1.0 / direction.y);
        double deltaDistZ = direction.z == 0 ? Double.POSITIVE_INFINITY : Math.abs(1.0 / direction.z);

        int stepX = (int) Math.signum(direction.x);
        int stepY = (int) Math.signum(direction.y);
        int stepZ = (int) Math.signum(direction.z);

        double sideDistX = stepX > 0
                ? (Math.floor(pos.x) + 1.0 - pos.x) * deltaDistX
                : (pos.x - Math.floor(pos.x)) * deltaDistX;
        if (stepX == 0) sideDistX = Double.POSITIVE_INFINITY;

        double sideDistY = stepY > 0
                ? (Math.floor(pos.y) + 1.0 - pos.y) * deltaDistY
                : (pos.y - Math.floor(pos.y)) * deltaDistY;
        if (stepY == 0) sideDistY = Double.POSITIVE_INFINITY;

        double sideDistZ = stepZ > 0
                ? (Math.floor(pos.z) + 1.0 - pos.z) * deltaDistZ
                : (pos.z - Math.floor(pos.z)) * deltaDistZ;
        if (stepZ == 0) sideDistZ = Double.POSITIVE_INFINITY;

        double distance = 0.0;
        double maxDistance = maxDistanceF;

        Block.Direction lastFace = Block.Direction.NONE;

        Block block = getBlock(blockPos.x, blockPos.y, blockPos.z);
        if (block != null && block.isSolid()) {
            return new BlockRaycastResult(blockPos.x, blockPos.y, blockPos.z, block, lastFace);
        }

        while (distance <= maxDistance) {
            if (sideDistX < sideDistY) {
                if (sideDistX < sideDistZ) {
                    blockPos.x += stepX;
                    distance = sideDistX;
                    sideDistX += deltaDistX;
                    lastFace = stepX > 0 ? Block.Direction.WEST : Block.Direction.EAST;
                } else {
                    blockPos.z += stepZ;
                    distance = sideDistZ;
                    sideDistZ += deltaDistZ;
                    lastFace = stepZ > 0 ? Block.Direction.SOUTH : Block.Direction.NORTH; // ← тут было наоборот
                }
            } else {
                if (sideDistY < sideDistZ) {
                    blockPos.y += stepY;
                    distance = sideDistY;
                    sideDistY += deltaDistY;
                    lastFace = stepY > 0 ? Block.Direction.DOWN : Block.Direction.UP;
                } else {
                    blockPos.z += stepZ;
                    distance = sideDistZ;
                    sideDistZ += deltaDistZ;
                    lastFace = stepZ > 0 ? Block.Direction.NORTH : Block.Direction.SOUTH; // ← и тут
                }
            }
            if (distance > maxDistance) {
                break;
            }

            block = getBlock(blockPos.x, blockPos.y, blockPos.z);
            if (block != null && block.isSolid()) {
                return new BlockRaycastResult(blockPos.x, blockPos.y, blockPos.z, block, lastFace);
            }
        }

        return null;
    }

    public ArrayList<AABB> getCubes(AABB boundingBox) {
        ArrayList<AABB> boundingBoxList = new ArrayList<>();

        int minX = (int) (Math.floor(boundingBox.minX) - 1);
        int maxX = (int) (Math.ceil(boundingBox.maxX) + 1);
        int minY = (int) (Math.floor(boundingBox.minY) - 1);
        int maxY = (int) (Math.ceil(boundingBox.maxY) + 1);
        int minZ = (int) (Math.floor(boundingBox.minZ) - 1);
        int maxZ = (int) (Math.ceil(boundingBox.maxZ) + 1);

        // Minimum level position
        minX = Math.max(0, minX);
        minY = Math.max(0, minY);
        minZ = Math.max(0, minZ);

        // Maximum level position
        maxX = Math.min(this.width, maxX);
        maxY = Math.min(this.depth, maxY);
        maxZ = Math.min(this.height, maxZ);

        // Include all surrounding tiles
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {

                    Block block = getBlock(x, y, z);
                    if (block != null) {

                        AABB aabb = block.getAABB(x, y, z);
                        if (aabb != null) {
                            boundingBoxList.add(aabb);
                        }
                    }
                }
            }
        }
        return boundingBoxList;
    }


}