package ru.mrbedrockpy.craftengine.graphics;

import lombok.Getter;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;
import ru.mrbedrockpy.craftengine.window.Camera;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL46C.*;

public class Mesh {
    private final int vaoId;
    private final List<Integer> vboIds = new ArrayList<>();
    private final int vertexCount;

    @Getter
    private final Shader shader;

    public Mesh(float[] positions, float[] texCoords) {
        this.vertexCount = positions.length / 3;
        this.shader = Shader.load("vertex.glsl", "fragment.glsl");

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, texCoords);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void storeDataInAttributeList(int attribIndex, int size, float[] data) {
        int vboId = glGenBuffers();
        vboIds.add(vboId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glEnableVertexAttribArray(attribIndex);
        glVertexAttribPointer(attribIndex, size, GL_FLOAT, false, 0, 0);
    }

    public void render(Matrix4f model, Matrix4f view, Matrix4f projection) {
        shader.use();
        shader.setUniformMatrix4f("model", model);
        shader.setUniformMatrix4f("view", view);
        shader.setUniformMatrix4f("projection", projection);

        glBindVertexArray(vaoId);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glBindVertexArray(0);
        for (int vboId : vboIds) {
            glDeleteBuffers(vboId);
        }
        glDeleteVertexArrays(vaoId);
        shader.dispose();
    }
}
