package ru.mrbedrockpy.craftengine.world;

import org.joml.Vector3f;
import org.joml.Vector3i;
import ru.mrbedrockpy.craftengine.graphics.Cuboid;
import ru.mrbedrockpy.craftengine.window.Camera;
import ru.mrbedrockpy.craftengine.world.block.Block;
import ru.mrbedrockpy.craftengine.world.raycast.BlockRaycastResult;

import java.util.ArrayList;
import java.util.List;

public class WorldRenderer {

    private final Cuboid[][][] cuboids;
    private final Camera camera;
    private final int width, height, depth;
    private Vector3i selectedBlock;

    public WorldRenderer(Camera camera, int width, int height, int depth) {
        this.camera = camera;
        this.width = width;
        this.height = height;
        this.depth = depth;

        cuboids = new Cuboid[width][height][depth];
    }

    public void addCuboid(int x, int y, int z, Cuboid cuboid) {
        if (inBounds(x, y, z)) {
            if (cuboids[x][y][z] != null) {
                cuboids[x][y][z].cleanup();
            }
            cuboids[x][y][z] = cuboid;
        }
    }

    public void removeCuboid(int x, int y, int z) {
        if (inBounds(x, y, z) && cuboids[x][y][z] != null) {
            cuboids[x][y][z].cleanup();
            cuboids[x][y][z] = null;
        }
    }

    private boolean inBounds(int x, int y, int z) {
        return x >= 0 && x < width
            && y >= 0 && y < height
            && z >= 0 && z < depth;
    }

    public void render(World world) {
        updateSelectedBlock(world);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    Block block = world.getBlock(x, y, z);
                    Cuboid cuboid = cuboids[x][y][z];

                    if (block != null && cuboid == null) {
                        cuboid = new Cuboid(new Vector3f(x, y, z), new Vector3f(1, 1, 1));
                        cuboids[x][y][z] = cuboid;
                    }

                    if (block == null && cuboid != null) {
                        cuboid.cleanup();
                        cuboids[x][y][z] = null;
                        continue;
                    }

                    if (cuboid != null) {
                        cuboid.render(camera.getViewMatrix(), camera.getProjectionMatrix());
                        if(selectedBlock != null && selectedBlock.equals(x, y, z)){
                            cuboid.renderOutline(camera.getViewMatrix(), camera.getProjectionMatrix());
                        }
                    }
                }
            }
        }

    }
    public void updateSelectedBlock(World world) {
            Vector3f origin = new Vector3f(camera.getPosition()).add(0, 1.8f, 0);
            Vector3f direction = camera.getFront();

            BlockRaycastResult blockRaycastResult = world.raycast(origin, direction, 4.5f);
            if(blockRaycastResult != null) {
                selectedBlock = new Vector3i(blockRaycastResult.x, blockRaycastResult.y, blockRaycastResult.z);
            } else {
                selectedBlock = null;
            }
        }


    public void cleanup() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    if (cuboids[x][y][z] != null) {
                        cuboids[x][y][z].cleanup();
                    }
                }
            }
        }
    }
}