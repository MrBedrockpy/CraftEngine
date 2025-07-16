package ru.mrbedrockpy.craftengine.world.block;

import org.joml.Vector3i;
import ru.mrbedrockpy.craftengine.graphics.Model;
import ru.mrbedrockpy.craftengine.graphics.Renderable;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_LINES;
import static org.lwjgl.opengl.GL11C.glDrawArrays;


public class Block implements Renderable {


    private final Model model;

    public Block(Vector3i position) {
        this.model = new BlockModel(position);
    }

    @Override
    public void render() {
        model.render();
    }
}
