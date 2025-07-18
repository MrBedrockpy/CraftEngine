package ru.mrbedrockpy.craftengine.gui.screen.widget;

import ru.mrbedrockpy.craftengine.gui.DrawContext;

import java.awt.*;

public class RectangleWidget extends AbstractWidget{
    private final Color color;
    public RectangleWidget(int x, int y, int width, int height, int zIndex, Color color) {
        super(x, y, width, height, zIndex);
        this.color = color;
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        x = mouseX;
        y = mouseY;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawRect(x, y, width, height, color);
    }
}
