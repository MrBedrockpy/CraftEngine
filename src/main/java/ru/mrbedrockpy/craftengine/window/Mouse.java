package ru.mrbedrockpy.craftengine.window;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class Mouse {
    private long window;

    private double lastX;
    private double lastY;
    private boolean firstMouse = true;

    @Getter
    private float deltaX;
    @Getter
    private float deltaY;

    @Setter
    private float sensitivity = 0.1f;

    private final boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
    private final boolean[] mouseButtonsPressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];

    public Mouse(long window) {
        this.window = window;

        double[] xpos = new double[1];
        double[] ypos = new double[1];
        GLFW.glfwGetCursorPos(window, xpos, ypos);
        lastX = xpos[0];
        lastY = ypos[0];

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        GLFW.glfwSetMouseButtonCallback(window, (win, button, action, mods) -> {
            if (button >= 0 && button < mouseButtons.length) {
                if (action == GLFW.GLFW_PRESS) {
                    mouseButtons[button] = true;
                    mouseButtonsPressed[button] = true;
                } else if (action == GLFW.GLFW_RELEASE) {
                    mouseButtons[button] = false;
                }
            }
        });
    }

    public void update() {
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        GLFW.glfwGetCursorPos(window, xpos, ypos);

        if (firstMouse) {
            lastX = xpos[0];
            lastY = ypos[0];
            firstMouse = false;
        }

        deltaX = (float) (xpos[0] - lastX);
        deltaY = (float) (lastY - ypos[0]);

        lastX = xpos[0];
        lastY = ypos[0];

        deltaX *= sensitivity;
        deltaY *= sensitivity;

        Arrays.fill(mouseButtonsPressed, false);
    }

    public boolean isButtonDown(int button) {
        return mouseButtons[button];
    }

    public boolean isButtonClicked(int button) {
        return mouseButtonsPressed[button];
    }
}