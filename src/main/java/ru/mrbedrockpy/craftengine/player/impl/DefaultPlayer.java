package ru.mrbedrockpy.craftengine.player.impl;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.graphics.Model;
import ru.mrbedrockpy.craftengine.player.Camera;
import ru.mrbedrockpy.craftengine.player.Player;

import java.util.HashSet;
import java.util.Set;

public class DefaultPlayer implements Player {

    private final Camera camera;

    private final Vector3f position;
    private final Vector2f rotation;

    private final Set<Model> models;

    private int heath;
    private int maxHeath;

    public DefaultPlayer(Camera camera, Vector3f position, Vector2f rotation, Set<Model> models, int maxHeath) {
        this.camera = camera;
        this.position = position;
        this.rotation = rotation;
        this.models = models;
        this.heath = maxHeath;
        this.maxHeath = maxHeath;
    }

    public DefaultPlayer(Camera camera, Vector3f position, Vector2f rotation, Set<Model> models, int heath, int maxHeath) {
        this.camera = camera;
        this.position = position;
        this.rotation = rotation;
        this.models = models;
        this.heath = heath;
        this.maxHeath = maxHeath;
    }

    @Override
    public Camera getCamera() {
        return this.camera;
    }

    @Override
    public int getHeath() {
        return this.heath;
    }

    @Override
    public int getMaxHeath() {
        return this.maxHeath;
    }

    @Override
    public void setHeath(int heath) {
        if (heath >= 0) this.heath = heath;
    }

    @Override
    public void setMaxHeath(int maxHeath) {
        if (maxHeath > 0) this.maxHeath = maxHeath;
    }

    @Override
    public void kill() {
        this.heath = 0;
    }

    @Override
    public boolean isDead() {
        return this.heath <= 0;
    }

    @Override
    public Vector3f getPosition() {
        return new Vector3f(this.position);
    }

    @Override
    public void move(Vector3f target) {
        this.position.add(target);
    }

    @Override
    public Vector2f getRotation() {
        return new Vector2f(rotation);
    }

    @Override
    public void rotate(Vector2f target) {

    }

    @Override
    public void render() {
        this.models.forEach(Model::render);
    }

    @Override
    public boolean hasModels() {
        return !this.models.isEmpty();
    }

    @Override
    public Set<Model> getModels() {
        return new HashSet<>(this.models);
    }
}
