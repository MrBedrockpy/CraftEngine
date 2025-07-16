package ru.mrbedrockpy.craftengine.graphics;

import java.util.Set;

public interface Model extends Renderable {

    boolean hasChildModels();

    Set<Model> getChildModels();

    Mesh getMesh();

    Shader getShader();

    Texture getTexture();

}
