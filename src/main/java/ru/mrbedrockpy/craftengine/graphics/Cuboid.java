package ru.mrbedrockpy.craftengine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.window.Camera;

public class Cuboid {

    private final Mesh mesh;
    private final Vector3f position;
    private final Vector3f size;
    private Texture texture;

    public Cuboid(Vector3f position, Vector3f size) {
        this.position = new Vector3f(position);
        this.size = new Vector3f(size);
        float[] vertices = generateVertices(position, size);
        float[] texCoords = generateTexCoords();
        this.mesh = new Mesh(vertices, texCoords);
        this.texture = Texture.load("block.png");
    }

    public void render(Matrix4f view, Matrix4f projection) {
        texture.use();
        mesh.render(view, projection);
    }

    public void cleanup() {
        mesh.cleanup();
    }

    private float[] generateVertices(Vector3f pos, Vector3f size) {
        float x = pos.x, y = pos.y, z = pos.z;
        float w = size.x / 2f, h = size.y / 2f, d = size.z / 2f;

        Vector3f[] v = {
            new Vector3f(x - w, y - h, z - d),
            new Vector3f(x + w, y - h, z - d),
            new Vector3f(x + w, y + h, z - d),
            new Vector3f(x - w, y + h, z - d),
            new Vector3f(x - w, y - h, z + d),
            new Vector3f(x + w, y - h, z + d),
            new Vector3f(x + w, y + h, z + d),
            new Vector3f(x - w, y + h, z + d)
        };

        int[] indices = {
            0, 1, 2, 2, 3, 0,
            5, 4, 7, 7, 6, 5,
            4, 0, 3, 3, 7, 4,
            1, 5, 6, 6, 2, 1,
            3, 2, 6, 6, 7, 3,
            4, 5, 1, 1, 0, 4
        };

        float[] vertices = new float[indices.length * 3];
        for (int i = 0; i < indices.length; i++) {
            Vector3f vert = v[indices[i]];
            vertices[i * 3] = vert.x;
            vertices[i * 3 + 1] = vert.y;
            vertices[i * 3 + 2] = vert.z;
        }

        return vertices;
    }


    private float[] generateTexCoords() {
        float[] uvFace = {
                0f, 0f, 1f, 0f, 1f, 1f,
                1f, 1f, 0f, 1f, 0f, 0f
        };

        float[] texCoords = new float[6 * 6 * 2];
        for (int i = 0; i < 6; i++) {
            System.arraycopy(uvFace, 0, texCoords, i * 12, 12);
        }
        return texCoords;
    }

}