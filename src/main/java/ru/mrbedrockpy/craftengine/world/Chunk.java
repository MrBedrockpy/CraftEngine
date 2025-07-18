package ru.mrbedrockpy.craftengine.world;

import lombok.Getter;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import ru.mrbedrockpy.craftengine.graphics.Cuboid;
import ru.mrbedrockpy.craftengine.graphics.Mesh;
import ru.mrbedrockpy.craftengine.world.block.Block;
import ru.mrbedrockpy.craftengine.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    public static final int WIDTH = 32;
    public static final int HEIGHT = 16;

    @Getter
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

    public void setEntities(List<LivingEntity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
    }

    public Mesh getChunkMesh() {
        List<Float> verticesList = new ArrayList<>();
        List<Float> texCoordsList = new ArrayList<>();
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                for(int z = 0; z < WIDTH; z++) {
                    Block block = getBlock(x, y, z);
                    if (block != null && block.isSolid()) {
                        Cuboid cuboid = new Cuboid(new Vector3f(x, y, z), new Vector3f(1, 1, 1));
                        float[] vertices = cuboid.generateVertices();
                        for (float vertex : vertices) {
                            verticesList.add(vertex);
                        }
                        float[] texCoords = cuboid.generateTexCoords();
                        for(float texCoord : texCoords) {
                            texCoordsList.add(texCoord);
                        }
                    }
                }
            }
        }
        float[] vertices = toFloatArray(verticesList);
        float[] texCoords = toFloatArray(texCoordsList);

        Mesh mesh = new Mesh(vertices, texCoords);
        return mesh;
    }
    public static float[] toFloatArray(List<Float> list) {
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public List<LivingEntity> getEntities() {
        return new ArrayList<>(entities);
    }
}
