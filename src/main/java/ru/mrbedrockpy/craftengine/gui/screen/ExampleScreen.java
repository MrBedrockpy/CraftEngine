package ru.mrbedrockpy.craftengine.gui.screen;

import ru.mrbedrockpy.craftengine.gui.screen.widget.RectangleWidget;

import java.awt.*;

public class ExampleScreen extends Screen {
    @Override
    public void init() {
        addWidget(new RectangleWidget(0, 0, 100, 100, 0, Color.RED));
    }
}
