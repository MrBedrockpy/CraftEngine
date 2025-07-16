package ru.mrbedrockpy.craftengine.entity;

public interface LivingEntity extends Entity {

    int getHeath();

    int getMaxHeath();

    void setHeath(int heath);

    void setMaxHeath(int heath);

    void kill();

    boolean isDead();

}
