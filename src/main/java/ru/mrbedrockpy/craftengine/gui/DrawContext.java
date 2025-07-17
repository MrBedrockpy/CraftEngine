package ru.mrbedrockpy.craftengine.gui;

import org.joml.Matrix4f;
import ru.mrbedrockpy.craftengine.graphics.Shader;
import ru.mrbedrockpy.craftengine.graphics.Texture;

import static org.lwjgl.opengl.GL46C.*;

// TODO: Add text rendering capabilities
public class DrawContext {
    private final Shader uiShader;
    private final int screenWidth;
    private final int screenHeight;

    private int vaoId;
    private int vboId;

    private final Matrix4f projection;

    public DrawContext(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        uiShader = Shader.load("ui_vertex.glsl", "ui_fragment.glsl");

        projection = new Matrix4f().ortho(0.0f, screenWidth, screenHeight, 0.0f, -1.0f, 1.0f);

        vaoId = glGenVertexArrays();
        vboId = glGenBuffers();

        glBindVertexArray(vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, 6 * 4 * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    public void drawTexture(int x, int y, float width, float height, Texture texture) {
        float[] vertices = {
                x,          y,          0.0f, 0.0f,
                x + width,  y,          1.0f, 0.0f,
                x + width,  y + height, 1.0f, 1.0f,

                x,          y,          0.0f, 0.0f,
                x + width,  y + height, 1.0f, 1.0f,
                x,          y + height, 0.0f, 1.0f
        };

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        uiShader.use();
        uiShader.setUniformMatrix4f("projection", projection);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getId());

        glBindVertexArray(vaoId);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);

        texture.unbind();
        uiShader.unbind();

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public void cleanup() {
        glDeleteVertexArrays(vaoId);
        glDeleteBuffers(vboId);
        uiShader.dispose();
    }
}
