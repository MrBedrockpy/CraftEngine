package ru.mrbedrockpy.craftengine.window;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;

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

    public Mouse(long window) {
        this.window = window;

        double[] xpos = new double[1];
        double[] ypos = new double[1];
        GLFW.glfwGetCursorPos(window, xpos, ypos);
        lastX = xpos[0];
        lastY = ypos[0];

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
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
    }

}