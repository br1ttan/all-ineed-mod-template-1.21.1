package com.hxrdsxk.item.custom;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import net.minecraft.text.Text;

public class GrowthToggleItem extends Item {
    private static final double MIN_SCALE = 0.3;
    private static final double MAX_SCALE = 2.5;

    private static final double SCALE_STEP = 0.1;

    public GrowthToggleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            EntityAttributeInstance scaleAttribute = user.getAttributeInstance(EntityAttributes.GENERIC_SCALE);
            if (scaleAttribute != null) {
                double currentScale = scaleAttribute.getValue();
                double newScale;

                if (user.isSneaking()) {
                    newScale = Math.min(currentScale + SCALE_STEP, MAX_SCALE);
                } else {
                    newScale = Math.max(currentScale - SCALE_STEP, MIN_SCALE);
                }

                scaleAttribute.setBaseValue(newScale);

                // Отправляем сообщение в чат
                if (newScale <= MIN_SCALE) {
                    user.sendMessage(Text.literal("Вы достигли минимального размера!"), false);
                } else if (newScale >= MAX_SCALE) {
                    user.sendMessage(Text.literal("Вы достигли максимального размера!"), false);
                } else if (Math.abs(newScale - 1.0) < 0.01) {
                    user.sendMessage(Text.literal("Вы вернули свой обычный рост!"), false);
                } else {
                    user.sendMessage(Text.literal(String.format("Ваш размер: %.1fx", newScale)), false);
                }
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
