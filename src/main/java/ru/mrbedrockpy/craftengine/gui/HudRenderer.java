package ru.mrbedrockpy.craftengine.gui;

import lombok.AllArgsConstructor;
import org.joml.Vector4f;

@AllArgsConstructor
public class HudRenderer {
    public int width;
    public int height;
    public void render(DrawContext context){
        context.drawRect(width / 2 , height / 2, 100, 100, new Vector4f(1, 1, 1,1));
    }
}
