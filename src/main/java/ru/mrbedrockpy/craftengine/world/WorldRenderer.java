package ru.mrbedrockpy.craftengine.world;

import ru.mrbedrockpy.craftengine.graphics.Cuboid;
import ru.mrbedrockpy.craftengine.window.Camera;

import java.util.ArrayList;
import java.util.List;

public class WorldRenderer {

    private final List<Cuboid> cuboids = new ArrayList<>();
    private final Camera camera;

    public WorldRenderer(Camera camera) {
        this.camera = camera;
    }

    public void addCuboid(Cuboid cuboid) {
        cuboids.add(cuboid);
    }

    public void removeCuboid(Cuboid cuboid) {
        cuboids.remove(cuboid);
    }

    public void render() {
        for (Cuboid cuboid : cuboids) {
            cuboid.render(camera.getViewMatrix(), camera.getProjectionMatrix());
        }
    }

    public void cleanup() {
        for (Cuboid cuboid : cuboids) {
            cuboid.cleanup();
        }
    }
}