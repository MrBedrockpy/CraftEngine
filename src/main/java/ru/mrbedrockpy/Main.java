package ru.mrbedrockpy;

import ru.mrbedrockpy.craftengine.window.WindowSettings;

public class Main extends AbstractGame {

    public Main() {
        super(new WindowSettings(1280, 720, "CraftEngine Game", true, false));
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
