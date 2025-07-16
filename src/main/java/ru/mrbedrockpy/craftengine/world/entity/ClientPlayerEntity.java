package ru.mrbedrockpy.craftengine.world.entity;

import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.window.Camera;
import ru.mrbedrockpy.craftengine.window.Input;
import ru.mrbedrockpy.craftengine.window.Mouse;
import ru.mrbedrockpy.craftengine.window.Window;
import ru.mrbedrockpy.craftengine.world.ClientWorld;

import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

public class ClientPlayerEntity extends Entity {
    @Getter
    private final Camera camera = new Camera();
    @Getter
    private final Mouse mouse;

    private final float speed = 0.01f;
    private boolean onGround = false;

    public ClientPlayerEntity(Vector3f position, Mouse mouse) {
        super(position, new Vector3f(1, 2, 1));
        this.mouse = mouse;
        this.camera.setPosition(position.add(0, 1.8f, 0));
    }

    @Override
    public void update(float deltaTime, ClientWorld world) {
        mouse.update();
        onGround = position.y < 1;
        camera.rotate(new Vector2f(mouse.getDeltaY(), mouse.getDeltaX()));

        Vector3f direction = new Vector3f();
        Vector3f front = getCameraFlatFront();
        Vector3f right = new Vector3f();
        front.cross(new Vector3f(0, 1, 0), right).normalize();

        if (Input.pressed(GLFW_KEY_A)) direction.add(new Vector3f(front).mul(speed));
        if (Input.pressed(GLFW_KEY_D)) direction.sub(new Vector3f(front).mul(speed));
        if (Input.pressed(GLFW_KEY_W)) direction.add(new Vector3f(right).mul(speed));
        if (Input.pressed(GLFW_KEY_S)) direction.sub(new Vector3f(right).mul(speed));
        if (Input.pressed(GLFW_KEY_SPACE)) direction.add(new Vector3f(0, speed, 0));

        if(!onGround){
            position.y -= speed * deltaTime * 100;
        }
        this.move(direction);
    }

    private void move(Vector3f direction) {
        camera.setPosition(position.add(direction));
    }

    @Override
    public void render(Camera camera) {
    }

    private Vector3f getCameraFlatFront() {
        float yaw = (float) Math.toRadians(camera.getAngle().y);

        Vector3f front = new Vector3f();
        front.x = (float) Math.sin(yaw);
        front.z = (float) -Math.cos(yaw);
        front.y = 0;

        return front.normalize();
    }

}
