package ru.mrbedrockpy.craftengine.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;
import ru.mrbedrockpy.craftengine.graphics.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL46C.*;

// TODO: Fix it
public class DrawContext {
    private final Shader uiShader;
    private final int screenWidth;
    private final int screenHeight;

    private int quadVaoId;
    private int quadVboId;
    private int quadEboId;

    public DrawContext(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        uiShader = Shader.load("ui_vertex.glsl", "ui_fragment.glsl");
        initQuad();
    }

    private void initQuad() {
        float[] vertices = {
            -0.5f, -0.5f, 0.0f, 0.0f,
             0.5f, -0.5f, 1.0f, 0.0f,
             0.5f,  0.5f, 1.0f, 1.0f,
            -0.5f,  0.5f, 0.0f, 1.0f
        };

        int[] indices = {
            0, 1, 2,
            2, 3, 0
        };

        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
        vertexBuffer.put(vertices).flip();

        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.length);
        indexBuffer.put(indices).flip();

        quadVaoId = glGenVertexArrays();
        quadVboId = glGenBuffers();
        quadEboId = glGenBuffers();

        glBindVertexArray(quadVaoId);

        glBindBuffer(GL_ARRAY_BUFFER, quadVboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, quadEboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);

        glBindVertexArray(0);

        MemoryUtil.memFree(vertexBuffer);
        MemoryUtil.memFree(indexBuffer);
    }

    private Vector2f pixelToNDC(float x, float y) {
        float ndcX = (2.0f * x) / screenWidth - 1.0f;
        float ndcY = 1.0f - (2.0f * y) / screenHeight;
        return new Vector2f(ndcX, ndcY);
    }

    public void drawRect(int x, int y, float width, float height, Vector4f color) {
        glDisable(GL_DEPTH_TEST);
        uiShader.use();

        float centerX = x + width / 2.0f;
        float centerY = y + height / 2.0f;

        Vector2f pos = pixelToNDC(centerX, centerY);
        Vector2f size = new Vector2f(
                2.0f * width / screenWidth,
                2.0f * height / screenHeight
        );

        uiShader.setUniform("uColor", color);
        uiShader.setUniform("uPosition", pos);
        uiShader.setUniform("uSize", size);

        glBindVertexArray(quadVaoId);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        glEnable(GL_DEPTH_TEST);
    }

    public void cleanup() {
        glDeleteBuffers(quadVboId);
        glDeleteBuffers(quadEboId);
        glDeleteVertexArrays(quadVaoId);
        uiShader.dispose();
    }
}