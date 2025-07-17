package ru.mrbedrockpy.craftengine.world;

import ru.mrbedrockpy.craftengine.graphics.Cuboid;
import ru.mrbedrockpy.craftengine.window.Camera;

import java.util.ArrayList;
import java.util.List;

public class WorldRenderer {

    private final Cuboid[][][] cuboids;
    private final Camera camera;
    private final int width, height, depth;

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

    public void render() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    Cuboid cuboid = cuboids[x][y][z];
                    if (cuboid != null) {
                        cuboid.render(camera.getViewMatrix(), camera.getProjectionMatrix());
                    }
                }
            }
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