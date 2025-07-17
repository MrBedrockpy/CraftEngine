package ru.mrbedrockpy.craftengine.world.entity;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.phys.AABB;
import ru.mrbedrockpy.craftengine.window.Camera;
import ru.mrbedrockpy.craftengine.world.ClientWorld;
import ru.mrbedrockpy.craftengine.world.World;

import java.util.List;

// TODO: Add normal collision detection
public abstract class LivingEntity {
    @Getter
    protected Vector3f position = new Vector3f();
    protected Vector3f velocity = new Vector3f();
    public Vector3f prevPosition = new Vector3f();
    @Getter
    protected Vector3f size = new Vector3f(1, 1, 1);
    protected int pitch;
    protected int yaw;
    protected World world;

    protected boolean onGround = false;

    // Minecraft-like gravity
    private final float gravity = -25f;
    private final float jumpStrength = 10.0f;
    protected float eyeHeight = 1.62f;
    public AABB boundingBox;
    protected Vector2f boundingBoxSize = new Vector2f(0.6f, 1.8f);

    public LivingEntity(Vector3f position, Vector3f size, World world) {
        setPosition(position);
        this.size.set(size);
        this.world = world;
    }

    public void setWorld(World world){
        this.world = world;
        this.world.addEntity(this);
    }

    public void update(float deltaTime, ClientWorld world){
//        if (!onGround) {
//            velocity.y += gravity * deltaTime;
//        }
//
//        position.add(new Vector3f(velocity).mul(deltaTime));
//
//        if (world.getBlock((int) position.x, (int) (position.y), (int) position.z) != null) {
//            velocity.y = 0;
//            onGround = true;
//        } else {
//            onGround = false;
//        }
    }
    public abstract void render(Camera camera);

    public void tick() {
        prevPosition = position;
    }

    public void move(Vector3d direction) {
        Vector3d prevPosition = direction;

        List<AABB> aABBs = this.world.getCubes(this.boundingBox.expand(direction));

        for (AABB abb : aABBs) {
            direction.y = abb.clipYCollide(this.boundingBox, direction.y);
        }
        this.boundingBox.move(0.0F, direction.y, 0.0F);

        for (AABB aABB : aABBs) {
            direction.x = aABB.clipXCollide(this.boundingBox, direction.x);
        }
        this.boundingBox.move(direction.x, 0.0F, 0.0F);

        for (AABB aABB : aABBs) {
            direction.z = aABB.clipZCollide(this.boundingBox, direction.z);
        }
        this.boundingBox.move(0.0F, 0.0F, direction.z);

        this.onGround = prevPosition.y != direction.y && prevPosition.y < 0.0F;

        if (prevPosition.x != direction.x) this.velocity.x = 0.0F;
        if (prevPosition.y != direction.y) this.velocity.y  = 0.0F;
        if (prevPosition.z != direction.z) this.velocity.z = 0.0F;

        setPosition(new Vector3f((float) ((this.boundingBox.minX + this.boundingBox.maxX) / 2.0D), (float) (this.boundingBox.minY + this.eyeHeight),
                (float) ((this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D)));
    }

    protected void moveRelative(float x, float z, float speed) {
        float distance = x * x + z * z;

        if (distance < 0.01F)
            return;

        distance = speed / (float) Math.sqrt(distance);
        x *= distance;
        z *= distance;

        double sin = Math.sin(Math.toRadians(this.yaw));
        double cos = Math.cos(Math.toRadians(this.yaw));

        velocity.set(x * cos - z * sin, velocity.y, z * cos + x * sin);
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
        this.boundingBox = new AABB(position.x - size.x, position.y - size.y,
                position.z - size.x, position.x + size.x,
                position.y + size.y, position.z + size.x);
    }


    public void jump() {
//        if (onGround) {
//            velocity.y = jumpStrength;
//            onGround = false;
//        }
    }
}