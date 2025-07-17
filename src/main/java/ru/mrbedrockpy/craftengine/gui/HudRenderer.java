package ru.mrbedrockpy.craftengine.gui;

import ru.mrbedrockpy.craftengine.graphics.Texture;

public class HudRenderer {
    public int width;
    public int height;

    public HudRenderer(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public Texture texture, hudTexture;
    public void render(DrawContext context){
        context.drawTexture(width / 2 - 25,height / 2 - 25, 50, 50, texture);
        context.drawTexture(width / 2 - hudTexture.getWidth() / 2 * 5, height - hudTexture.getHeight() * 5,
                hudTexture.getWidth() * 5, hudTexture.getHeight() * 5, hudTexture);
    }
}
