#version 460 core
layout (location = 0) in vec2 aPos; // [-1, 1] прямоугольник (обычный quad)

uniform vec2 uPosition; // Положение в NDC (от -1 до 1)
uniform vec2 uSize;     // Размер в NDC

void main() {
    vec2 scaledPos = aPos * uSize;
    vec2 finalPos = scaledPos + uPosition;
    gl_Position = vec4(finalPos, 0.0, 1.0);
}