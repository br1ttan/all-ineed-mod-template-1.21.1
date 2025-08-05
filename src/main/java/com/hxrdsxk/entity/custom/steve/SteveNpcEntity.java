package com.hxrdsxk.entity.custom.steve;

import com.hxrdsxk.entity.custom.steve.goals.FollowPlayerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SteveNpcEntity extends PathAwareEntity {

    private PlayerEntity followTarget = null;

    public SteveNpcEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setFollowTarget(PlayerEntity player) {
        this.followTarget = player;
    }

    public PlayerEntity getFollowTarget() {
        return followTarget;
    }

    public static DefaultAttributeContainer.Builder createSteveAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
    }


    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getWorld().isClient && player instanceof ServerPlayerEntity serverPlayer) {
            player.sendMessage(Text.literal("Выберите поведение NPC: [1] Следовать [2] Гулять [3] Стоять"), false);

            // Пример: простая смена режима без GUI
            ItemStack stack = player.getStackInHand(hand);
            String itemName = stack.getItem().toString();

            if (itemName.contains("stick")) { // если в руке палка, то пусть будет переключатель
                if (this.getFollowTarget() == null) {
                    this.setFollowTarget(serverPlayer);
                    player.sendMessage(Text.literal("Теперь NPC будет следовать за вами."), false);
                } else {
                    this.setFollowTarget(null);
                    player.sendMessage(Text.literal("NPC больше не следует за вами."), false);
                }
            }

            this.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new FollowPlayerGoal(this, 1.0, 2.0f));

        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.6));
    }

}
