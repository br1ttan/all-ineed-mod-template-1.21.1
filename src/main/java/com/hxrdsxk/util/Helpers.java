package com.hxrdsxk.util;

import net.minecraft.world.World;

public class Helpers {
    public static float getRandomChanceByLevel(World world, int level) {
        float chance;

        switch (level) {
            case 1 -> chance = 0.05f;
            case 2 -> chance = 0.07f;
            case 3 -> chance = 0.08f;
            default -> chance = 0.0f; // на всякий случай
        }

        return chance;
    }
}
