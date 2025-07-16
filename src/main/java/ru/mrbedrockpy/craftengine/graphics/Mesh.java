package ru.mrbedrockpy.craftengine.graphics;

import org.lwjgl.system.MemoryUtil;
import ru.mrbedrockpy.craftengine.window.Camera;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46C.*;

public class Mesh {

    private final int vaoId;
    private final int vboId;
    private final int vertexCount;

    private final Shader shader;

    public Mesh(float[] positions) {
        vertexCount = positions.length / 3;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        FloatBuffer buffer = MemoryUtil.memAllocFloat(positions.length);
        buffer.put(positions).flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);

        int stride = 3 * Float.BYTES;
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        shader = Shader.load("vertex.glsl", "fragment.glsl");
    }

    public void render(Camera camera) {
        shader.use();
        shader.setUniformMatrix4f("view", camera.getViewMatrix());
        shader.setUniformMatrix4f("projection", camera.getProjectionMatrix());
        glBindVertexArray(vaoId);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);
        // glDisableVertexAttribArray(1); // Удалить тоже
        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);
        shader.dispose();
    }
}