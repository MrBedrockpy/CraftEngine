package ru.mrbedrockpy.craftengine.world;

import ru.mrbedrockpy.craftengine.entity.Entity;
import ru.mrbedrockpy.craftengine.graphics.Renderable;

import java.util.List;

public interface World extends Renderable {

    List<Entity> getEntities();

}
