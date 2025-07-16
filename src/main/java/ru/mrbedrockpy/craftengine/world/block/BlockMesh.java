package ru.mrbedrockpy.craftengine.world.block;

import lombok.AllArgsConstructor;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46C;
import ru.mrbedrockpy.craftengine.graphics.Mesh;

@AllArgsConstructor
public class BlockMesh implements Mesh {
    private final int vao;
    private final int vbo;

    @Override
    public int getVAO() {
        return vao;
    }

    @Override
    public int getVBO() {
        return vbo;
    }

    @Override
    public int getVertices() {
        return 8;
    }

    @Override
    public int getDrawPrimitive() {
        return GL11.GL_TRIANGLES;
    }

    @Override
    public void dispose() {
        GL46C.glDeleteVertexArrays(vao);
        GL46C.glDeleteBuffers(vbo);
    }
}
