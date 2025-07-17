package ru.mrbedrockpy.craftengine.world.raycast;

import lombok.AllArgsConstructor;
import ru.mrbedrockpy.craftengine.world.block.Block;

@AllArgsConstructor
public class BlockRaycastResult {
    public final int x, y, z;
    public final Block block;
    public final Block.Direction direction;
}