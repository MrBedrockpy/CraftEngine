package ru.mrbedrockpy.craftengine.world.entity;

import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import ru.mrbedrockpy.craftengine.event.MouseClickEvent;
import ru.mrbedrockpy.craftengine.window.Camera;
import ru.mrbedrockpy.craftengine.window.Input;
import ru.mrbedrockpy.craftengine.world.ClientWorld;
import ru.mrbedrockpy.craftengine.world.raycast.BlockRaycastResult;

import static org.lwjgl.glfw.GLFW.*;

public class ClientPlayerEntity extends LivingEntity {
    @Getter
    private final Camera camera = new Camera();
    private final float speed = 0.045f;
    private final float sensitivity = 20.0f;

    public ClientPlayerEntity(Vector3f position, ClientWorld world) {
        super(position, new Vector3f(1, 2, 1), world);
        this.camera.setPosition(position.add(0, 1.8f, 0));
    }

    @Override
    public void update(float deltaTime, ClientWorld world) {
        super.update(deltaTime, world);
        camera.rotate(new Vector2f(
                (float) -Input.getDeltaY() * sensitivity * deltaTime,
                (float) Input.getDeltaX() * sensitivity * deltaTime
        ));

        Vector3f direction = new Vector3f();
        Vector3f front = camera.getFlatFront();
        Vector3f right = new Vector3f();
        front.cross(new Vector3f(0, 1, 0), right).normalize();

        if (Input.pressed(GLFW_KEY_A)) direction.add(new Vector3f(front).mul(speed));
        if (Input.pressed(GLFW_KEY_D)) direction.sub(new Vector3f(front).mul(speed));
        if (Input.pressed(GLFW_KEY_W)) direction.add(new Vector3f(right).mul(speed));
        if (Input.pressed(GLFW_KEY_S)) direction.sub(new Vector3f(right).mul(speed));
        if (Input.pressed(GLFW_KEY_SPACE)) jump();

        this.move(direction);
    }

    private void move(Vector3f direction) {
        camera.setPosition(position.add(direction));
    }

    @Override
    public void render(Camera camera) {
    }


    public void onMouseClick(MouseClickEvent event) {
        if (event.getButton() == GLFW_MOUSE_BUTTON_LEFT) {
            Vector3f eyePos = new Vector3f(position.x, position.y + 1.8f, position.z);
            BlockRaycastResult blockRaycastResult = world.raycast(eyePos, camera.getFront(), 4.5f);
            if(blockRaycastResult != null){
                world.setBlock(blockRaycastResult.x, blockRaycastResult.y, blockRaycastResult.z, null);
            }
        } else if (event.getButton() == GLFW_MOUSE_BUTTON_RIGHT) {}
    }
}
