package ru.mrbedrockpy.craftengine.world.raycast;

import ru.mrbedrockpy.craftengine.world.block.Block;

public class BlockRaycastResult {
    public final int x, y, z;
    public final Block block;

    public BlockRaycastResult(int x, int y, int z, Block block) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
    }
}