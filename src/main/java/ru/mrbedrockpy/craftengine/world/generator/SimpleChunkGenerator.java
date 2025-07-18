package ru.mrbedrockpy.craftengine.world.generator;

import org.joml.Vector2i;
import ru.mrbedrockpy.craftengine.world.Chunk;
import ru.mrbedrockpy.craftengine.world.block.Block;

public class SimpleChunkGenerator implements ChunkGenerator {

    @Override
    public void generate(Vector2i chunkPos, Chunk chunk) {
        for (int x = 0; x < Chunk.WIDTH; x++) {
            for (int y = 0; y < Chunk.HEIGHT; y++) {
                for (int z = 0; z < Chunk.WIDTH; z++) {
                    chunk.setBlock(x, y, z, new Block(true));
                }
            }
        }
    }
}
