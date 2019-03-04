#version 330 core

in vec2 outTexCoord;
out vec4 outColor;

uniform sampler2D texture_sampler;

void main() {
    outColor = texture(texture_sampler, outTexCoord);
}