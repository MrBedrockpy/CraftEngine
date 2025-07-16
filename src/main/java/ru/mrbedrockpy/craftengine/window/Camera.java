package ru.mrbedrockpy.craftengine.window;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Vector3f position = new Vector3f(0,0,0);
    private final Vector2f angle = new Vector2f(0,0); // pitch (x), yaw (y)

    private final float fov = (float) Math.toRadians(110.0);
    private final float aspectRatio = 16f / 9f;
    private final float zNear = 0.1f;
    private final float zFar = 1000f;

    public Camera() {
        updateProjectionMatrix();
        updateViewMatrix();
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
        updateViewMatrix();
    }

    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    public void move(Vector3f delta) {
        position.add(delta);
        updateViewMatrix();
    }

    public void setAngle(Vector2f angle) {
        this.angle.set(angle);
        updateViewMatrix();
    }

    public Vector2f getAngle() {
        return new Vector2f(angle);
    }

    public void rotate(Vector2f deltaAngle) {
        angle.add(deltaAngle);
        clampAngles();
        updateViewMatrix();
    }

    private void clampAngles() {
        if (angle.x > 89f) angle.x = 89f;
        if (angle.x < -89f) angle.x = -89f;

        if (angle.y > 180f) angle.y -= 360f;
        else if (angle.y < -180f) angle.y += 360f;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f(viewMatrix);
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f(projectionMatrix);
    }

    public void updateProjectionMatrix() {
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
    }

    public void updateViewMatrix() {
        Vector3f front = new Vector3f();

        float pitchRad = (float) Math.toRadians(angle.x);
        float yawRad = (float) Math.toRadians(angle.y);

        front.x = (float) (Math.cos(pitchRad) * Math.cos(yawRad));
        front.y = (float) Math.sin(pitchRad);
        front.z = (float) (Math.cos(pitchRad) * Math.sin(yawRad));
        front.normalize();

        Vector3f eyePosition = new Vector3f(position).add(0, 1.8f, 0);
        Vector3f center = new Vector3f(eyePosition).add(front);
        Vector3f up = new Vector3f(0, 1, 0);

        viewMatrix.identity();
        viewMatrix.lookAt(eyePosition, center, up);
    }

}