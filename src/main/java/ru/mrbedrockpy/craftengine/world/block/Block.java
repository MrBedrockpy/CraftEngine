package ru.mrbedrockpy.craftengine.world.block;


import ru.mrbedrockpy.craftengine.phys.AABB;

public class Block {
    private final boolean solid;

    public Block(boolean solid) {
        this.solid = solid;
    }

    public boolean isSolid() {
        return solid;
    }

    public AABB getAABB(int x, int y, int z) {
        return new AABB(x, y, z, x + 1, y + 1, z + 1);
    }

    public enum Direction {
        UP,
        DOWN,
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NONE
    }
}