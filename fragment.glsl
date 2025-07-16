#version 460

layout (location = 0) out vec4 color;

in vec3 position;
in vec4 colour;

void main() {
    color = colour;
}
