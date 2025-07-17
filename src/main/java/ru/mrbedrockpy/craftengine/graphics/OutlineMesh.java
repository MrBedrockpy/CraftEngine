package ru.mrbedrockpy.craftengine.graphics;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL46C.*;

public class OutlineMesh {

    private final int vaoId;
    private final int vboId;
    private final int vertexCount;

    private final Shader shader;

    public OutlineMesh(float[] positions, float[] texCoords) {
        this.vertexCount = positions.length / 3;

        shader = Shader.load("outline_vertex.glsl", "outline_fragment.glsl");

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        int texVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, texVboId);
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render(Matrix4f view, Matrix4f projection) {
        shader.use();
        shader.setUniformMatrix4f("view", view);
        shader.setUniformMatrix4f("projection", projection);

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
    public void render(Matrix4f model, Matrix4f view, Matrix4f projection) {
        shader.use();
        shader.setUniformMatrix4f("model", model);
        shader.setUniformMatrix4f("view", view);
        shader.setUniformMatrix4f("projection", projection);

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);
        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);
        shader.dispose();
    }
}
