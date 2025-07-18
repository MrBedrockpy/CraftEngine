package ru.mrbedrockpy.craftengine.graphics;

import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import ru.mrbedrockpy.craftengine.window.Camera;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL46C.*;

public class Mesh {

    private final int vaoId;
    private final int vboId;
    private final int uvboId;
    private final int eboId;
    private final int indexCount;
//    public Mesh(float[] vertices, float[] texCoords) {
//        this(vertices, texCoords, null);
//    }

    public Mesh(float[] vertices, float[] texCoords, int[] indices) {
        indexCount = indices.length;
//        debugPrint(indices.length, vertices, texCoords, indices);

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        uvboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, uvboId);
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void bind() {
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    public void unbind() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void render() {
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public static Mesh mergeMeshes(List<MeshData> meshDataList) {
        List<Float> combinedPositions = new ArrayList<>();
        List<Float> combinedUVs = new ArrayList<>();
        List<Integer> combinedIndices = new ArrayList<>();

        int vertexOffset = 0;

        for (MeshData data : meshDataList) {
            float[] positions = data.positions;
            float[] uvs = data.uvs;
            int[] indices = data.indices;

            for (int i = 0; i < positions.length; i += 3) {
                combinedPositions.add(positions[i]);
                combinedPositions.add(positions[i + 1]);
                combinedPositions.add(positions[i + 2]);
            }

            for (int i = 0; i < uvs.length; i += 2) {
                combinedUVs.add(uvs[i]);
                combinedUVs.add(uvs[i + 1]);
            }

            for (int index : indices) {
                combinedIndices.add(index + vertexOffset);
            }

            vertexOffset += positions.length / 3;
        }


        float[] finalPositions = new float[combinedPositions.size()];
        float[] finalUVs = new float[combinedUVs.size()];
        int[] finalIndices = new int[combinedIndices.size()];

        for (int i = 0; i < finalPositions.length; i++) finalPositions[i] = combinedPositions.get(i);
        for (int i = 0; i < finalUVs.length; i++) finalUVs[i] = combinedUVs.get(i);
        for (int i = 0; i < finalIndices.length; i++) finalIndices[i] = combinedIndices.get(i);
        System.out.println("Final mesh data: " + finalPositions.length + " positions, " + finalUVs.length + " UVs, " + finalIndices.length + " indices");

        return new Mesh(finalPositions, finalUVs, finalIndices);
    }

    public void cleanup() {
        glDeleteVertexArrays(vaoId);
    }

    public record MeshData(float[] positions, float[] uvs, int[] indices){}
}