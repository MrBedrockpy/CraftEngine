package ru.mrbedrockpy.craftengine.player;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public interface Camera {

    Vector3f getPosition();

    void move(Vector3f target);

    Vector2f getRotation();

    void rotate(Vector2f target);

    float getFov();

    void setFov(float fov);

    float getZNear();

    void setZNear(float zNear);

    float getZFar();

    void setZFar(float zFar);

    Matrix4f getView();

    Matrix4f getProjection();

}
