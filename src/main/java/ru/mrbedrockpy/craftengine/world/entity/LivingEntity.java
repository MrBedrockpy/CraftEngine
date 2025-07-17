package ru.mrbedrockpy.craftengine.world.entity;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.window.Camera;
import ru.mrbedrockpy.craftengine.world.ClientWorld;
import ru.mrbedrockpy.craftengine.world.World;

// TODO: Add normal collision detection
public abstract class LivingEntity {
    @Getter
    protected Vector3f position = new Vector3f();
    protected Vector3f velocity = new Vector3f();
    @Getter
    protected Vector3f size = new Vector3f(1, 1, 1);
    protected int pitch;
    protected int yaw;
    protected World world;

    private boolean onGround = false;

    // Minecraft-like gravity
    private final float gravity = -25f;
    private final float jumpStrength = 10.0f;

    public LivingEntity(Vector3f position, Vector3f size, World world) {
        this.position.set(position);
        this.size.set(size);
        this.world = world;
    }

    public void setWorld(World world){
        this.world = world;
        this.world.addEntity(this);
    }

    public void update(float deltaTime, ClientWorld world){
        if (!onGround) {
            velocity.y += gravity * deltaTime;
        }

        position.add(new Vector3f(velocity).mul(deltaTime));

        if (world.getBlock((int) position.x, (int) (position.y), (int) position.z) != null) {
            velocity.y = 0;
            onGround = true;
        } else {
            onGround = false;
        }
    }
    public abstract void render(Camera camera);

    public void tick() {
    }

    public void jump() {
        if (onGround) {
            velocity.y = jumpStrength;
            onGround = false;
        }
    }

}