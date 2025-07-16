package ru.mrbedrockpy.craftengine.player.impl;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.player.Camera;
import ru.mrbedrockpy.craftengine.window.Window;

public class DefaultCamera implements Camera {

    private final Vector3f position;
    private final Vector2f rotation;
    private final Matrix4f viewMatrix;
    private final Matrix4f projectionMatrix;
    
    private float fov;
    private float zNear;
    private float zFar;
    
    private final Vector3f front = new Vector3f();
    private final Vector3f up = new Vector3f(0, 1, 0);
    private final Vector3f right = new Vector3f();
    private final Vector3f worldUp = new Vector3f(0, 1, 0);
    
    public DefaultCamera(Vector3f position, Vector2f rotation, float fov, float zNear, float zFar) {
        this.position = new Vector3f(position);
        this.rotation = new Vector2f(rotation);
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;
        
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
        
        updateCameraVectors();
        updateViewMatrix();
        updateProjectionMatrix();
    }
    
    private void updateCameraVectors() {
        float pitch = rotation.x();
        float yaw = rotation.y();

        front.x = (float) (Math.cos(yaw) * Math.cos(pitch));
        front.y = (float) Math.sin(pitch);
        front.z = (float) (Math.sin(yaw) * Math.cos(pitch));
        front.normalize();

        front.cross(worldUp, right).normalize();
        right.cross(front, up).normalize();
    }
    
    private void updateViewMatrix() {
        Vector3f center = new Vector3f(position).add(front);
        viewMatrix.identity().lookAt(position, center, up);
    }
    
    private void updateProjectionMatrix() {
        projectionMatrix.identity().perspective(
                (float) Math.toRadians(fov),
                ((float) Window.getWidth()) / ((float) Window.getHeight()),
                zNear,
                zFar
        );
    }
    
    @Override
    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    @Override
    public void move(Vector3f target) {
        position.set(target);
        updateViewMatrix();
    }

    @Override
    public Vector2f getRotation() {
        return new Vector2f(rotation);
    }

    @Override
    public void rotate(Vector2f target) {
        float pitch = target.x();
        float maxPitch = (float) Math.toRadians(89.9);
        rotation.set(
            Math.min(maxPitch, Math.max(-maxPitch, pitch)),
            target.y()
        );
        updateCameraVectors();
        updateViewMatrix();
    }

    @Override
    public float getFov() {
        return fov;
    }

    @Override
    public void setFov(float fov) {
        this.fov = fov;
        updateProjectionMatrix();
    }

    @Override
    public float getZNear() {
        return zNear;
    }

    @Override
    public void setZNear(float zNear) {
        this.zNear = zNear;
        updateProjectionMatrix();
    }

    @Override
    public float getZFar() {
        return zFar;
    }

    @Override
    public void setZFar(float zFar) {
        this.zFar = zFar;
        updateProjectionMatrix();
    }

    @Override
    public Matrix4f getView() {
        return new Matrix4f(viewMatrix);
    }

    @Override
    public Matrix4f getProjection() {
        return new Matrix4f(projectionMatrix);
    }
}