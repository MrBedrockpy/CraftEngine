package ru.mrbedrockpy.craftengine.gui.screen;

import ru.mrbedrockpy.craftengine.gui.DrawContext;
import ru.mrbedrockpy.craftengine.gui.screen.widget.AbstractWidget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Screen {
    private final List<AbstractWidget> widgets = new ArrayList<>();

    public abstract void init();
    public void onClose() {}

    public int addWidget(AbstractWidget widget) {
        widgets.add(widget);
        widgets.sort(Comparator.comparingInt(AbstractWidget::getZIndex));
        return widgets.indexOf(widget);
    }

    public void removeWidget(int index) {
        widgets.remove(index);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for(AbstractWidget widget : widgets) {
            if (widget.isVisible()) {
                widget.render(context, mouseX, mouseY, delta);
            }
        }
    }
}
