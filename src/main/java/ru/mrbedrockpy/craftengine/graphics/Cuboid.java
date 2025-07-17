package ru.mrbedrockpy.craftengine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.window.Camera;

import static org.lwjgl.opengl.GL46C.*;


public class Cuboid {

    private final Mesh mesh;
    private final Vector3f position;
    private final Vector3f size;
    private Texture texture;
    private int outlineVao, outlineVbo, outlineEbo;
    private Shader outlineShader;
    private static final int[] LINE_INDICES = {
            0,1, 1,2, 2,3, 3,0,
            4,5, 5,6, 6,7, 7,4,
            0,4, 1,5, 2,6, 3,7
    };
    private static final float[] LINE_VERTICES = {
            0, 0, 0,
            1, 0, 0,
            1, 1, 0,
            0, 1, 0,
            0, 0, 1,
            1, 0, 1,
            1, 1, 1,
            0, 1, 1
    };

    public Cuboid(Vector3f position, Vector3f size) {
        this.position = new Vector3f(position);
        this.size = new Vector3f(size);
        float[] vertices = generateVertices(position, size);
        float[] texCoords = generateTexCoords();
        this.mesh = new Mesh(vertices, texCoords);
        this.texture = Texture.load("block.png");
        outlineVao = glGenVertexArrays();
        outlineVbo = glGenBuffers();
        outlineEbo = glGenBuffers();

        glBindVertexArray(outlineVao);

        glBindBuffer(GL_ARRAY_BUFFER, outlineVbo);
        glBufferData(GL_ARRAY_BUFFER, LINE_VERTICES, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, outlineEbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, LINE_INDICES, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);

        outlineShader = Shader.load("outline_vertex.glsl", "outline_fragment.glsl");
    }

    public Matrix4f getModelMatrix() {
        return new Matrix4f()
                .translate(position)
                .scale(size);
    }

    public void render(Matrix4f view, Matrix4f projection) {
        texture.use();
        mesh.render(view, projection);
    }

    public void renderOutline(Matrix4f view, Matrix4f projection) {
        outlineShader.use();

        outlineShader.setUniformMatrix4f("model", getModelMatrix());
        outlineShader.setUniformMatrix4f("view", view);
        outlineShader.setUniformMatrix4f("projection", projection);

        glBindVertexArray(outlineVao);
        glDrawElements(GL_LINES, LINE_INDICES.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        outlineShader.unbind();
    }

    public void cleanup() {
        mesh.cleanup();
        glDeleteVertexArrays(outlineVao);
        glDeleteBuffers(outlineVbo);
        glDeleteBuffers(outlineEbo);
        outlineShader.dispose();
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