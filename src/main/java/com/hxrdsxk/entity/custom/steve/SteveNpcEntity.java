package com.hxrdsxk.entity.custom.steve;

import com.hxrdsxk.entity.ModEntities;
import com.hxrdsxk.entity.custom.steve.goals.MagicRitualGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SteveNpcEntity extends PathAwareEntity {
    private PlayerEntity followTarget = null;
    private final SimpleInventory inventory = new SimpleInventory(10);

    public SteveNpcEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.inventory.setStack(1, new ItemStack(Items.IRON_SWORD));

        this.inventory.setStack(4, new ItemStack(Items.STONE_PICKAXE));

    }

    public void setFollowTarget(PlayerEntity player) {
        this.followTarget = player;
    }

    public PlayerEntity getFollowTarget() {
        return followTarget;
    }

    public SimpleInventory getInventory() {
        return inventory;
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

            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() == Items.STICK) {
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

    public boolean hasPickaxe() {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty() && stack.getItem() instanceof PickaxeItem) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getPickaxe() {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty() && stack.getItem() instanceof PickaxeItem) {
                return stack.copy();
            }
        }
        return ItemStack.EMPTY;
    }

    public void equipPickaxeInHand() {
        ItemStack pickaxe = getPickaxe();
        if (!pickaxe.isEmpty()) {
            this.equipStack(EquipmentSlot.MAINHAND, pickaxe);
            this.setStackInHand(Hand.MAIN_HAND, pickaxe);
            this.sendMessage(Text.literal("[MineGoal] Кирка экипирована: " + pickaxe));
        } else {
            this.sendMessage(Text.literal("[MineGoal] Не удалось экипировать кирку"));
        }
    }

    @Override
    protected void initGoals() {
//        this.goalSelector.add(1, new MineAndStoreGoal(this));
        this.goalSelector.add(3, new MagicRitualGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.6));
    }

    public void sendMessage(Text text) {
        if (!this.getWorld().isClient) {
            this.getWorld().getPlayers().forEach(player -> player.sendMessage(text, false));
        }
    }
}