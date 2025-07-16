package ru.mrbedrockpy.craftengine.world.entity;

import lombok.Getter;
import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.window.Camera;
import ru.mrbedrockpy.craftengine.world.ClientWorld;

public abstract class Entity {
    @Getter
    protected Vector3f position = new Vector3f();
    protected Vector3f velocity = new Vector3f();
    @Getter
    protected Vector3f size = new Vector3f(1, 1, 1);

    public Entity(Vector3f position, Vector3f size) {
        this.position.set(position);
        this.size.set(size);
    }

    public abstract void update(float deltaTime, ClientWorld world);
    public abstract void render(Camera camera);

}