#version 460 core

in vec2 fragUV;
out vec4 fragColor;

uniform sampler2D uiTexture;

void main() {
    fragColor = texture(uiTexture, fragUV);
}