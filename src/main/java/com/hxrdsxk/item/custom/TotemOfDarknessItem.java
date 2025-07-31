package com.hxrdsxk.item.custom;

import com.hxrdsxk.AllINeedMod;
import com.hxrdsxk.item.payload.ActivateTotemPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;


public class TotemOfDarknessItem extends Item {
    public TotemOfDarknessItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user instanceof ServerPlayerEntity player) {
            activateTotem(world, player, hand);
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    public static void activateTotem(World world, ServerPlayerEntity player, Hand hand) {
        player.setHealth(8.0F);
        player.getHungerManager().add(6, 0.5F);

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 5 * 30, 0));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 5 * 30, 1));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 5 * 20, 4));

        player.playSound(SoundEvents.ENTITY_PHANTOM_FLAP, 1.0F, 0.5F);

        // Частицы
        for (int i = 0; i < 100; i++) {
            Vec3d pos = player.getPos().add(
                    (player.getRandom().nextDouble() - 0.5) * 2.5,
                    player.getRandom().nextDouble() * 2.0,
                    (player.getRandom().nextDouble() - 0.5) * 2.5
            );
            player.getServerWorld().spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            player.getServerWorld().spawnParticles(ParticleTypes.REVERSE_PORTAL, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
        }

        player.getWorld().playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.ITEM_TOTEM_USE,
                player.getSoundCategory(),
                1.0F, 1.0F
        );

        ServerPlayNetworking.send(player, new ActivateTotemPayload());
        ItemStack stack = player.getStackInHand(hand);


        stack.decrement(1);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(AllINeedMod.MOD_ID + "totem_of_darkness.tooltip"));
    }
}
