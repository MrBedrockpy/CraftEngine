package ru.mrbedrockpy.craftengine.world.block;

public class Block {
    private final boolean solid;

    public Block(boolean solid) {
        this.solid = solid;
    }

    public boolean isSolid() {
        return solid;
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