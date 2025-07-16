package ru.mrbedrockpy.craftengine.world.block;

import lombok.AllArgsConstructor;
import org.joml.Matrix4f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL46C;
import ru.mrbedrockpy.craftengine.graphics.Mesh;
import ru.mrbedrockpy.craftengine.graphics.Model;
import ru.mrbedrockpy.craftengine.graphics.Shader;
import ru.mrbedrockpy.craftengine.graphics.Texture;

import java.util.Set;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_LINES;
import static org.lwjgl.opengl.GL11C.glDrawArrays;

public class BlockModel implements Model {

    private final Vector3i position;
    private final BlockMesh mesh;

    public BlockModel(Vector3i position){
        this.position = position;
        this.mesh = new BlockMesh(GL46C.glGenVertexArrays(), GL46C.glGenBuffers());
    }
    @Override
    public boolean hasChildModels() {
        return false;
    }

    @Override
    public Set<Model> getChildModels() {
        return Set.of();
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public Shader getShader() {
        return Shader.load("vertex.glsl", "fragment.glsl");
    }

    @Override
    public Texture getTexture() {
        return Texture.load("block.png");
    }

    @Override
    public void render() {
        Shader shader = getShader();
        shader.use(); // активируем шейдер

        // создаём модельную матрицу (позиционируем блок в мире)
        Matrix4f modelMatrix = new Matrix4f().translation(position.x, position.y, position.z);

        // передаём матрицу в шейдер
        shader.setUniformMatrix4f("model", modelMatrix);

        // текстура
        Texture texture = getTexture();
        texture.use();

        // отрисовка меша
        glBindVertexArray(mesh.getVAO());
        glDrawArrays(GL_LINES, 0, mesh.getVertices());
        glBindVertexArray(0);

        texture.unbind();
        shader.unbind();
    }
}
