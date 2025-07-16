package ru.mrbedrockpy.craftengine.entity;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.graphics.Model;
import ru.mrbedrockpy.craftengine.graphics.Renderable;

import java.util.Set;

public interface Entity extends Renderable {

    boolean hasModels();

    Set<Model> getModels();

    Vector3f getPosition();

    void move(Vector3f target);

    Vector2f getRotation();

    void rotate(Vector2f target);

}
