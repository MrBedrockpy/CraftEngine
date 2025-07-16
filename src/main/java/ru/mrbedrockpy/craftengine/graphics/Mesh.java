package ru.mrbedrockpy.craftengine.graphics;

public interface Mesh extends Disposable {

    int getVAO();

    int getVBO();

    int getVertices();

    int getDrawPrimitive();

}
