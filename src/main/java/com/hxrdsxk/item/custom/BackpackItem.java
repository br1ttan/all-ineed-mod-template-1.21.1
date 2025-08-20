package com.hxrdsxk.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BackpackItem extends ArmorItem {
    private static final Text TITLE = Text.literal("Backpack");

    public BackpackItem(ArmorMaterial material, Settings settings) {
        super(RegistryEntry.of(material), ArmorItem.Type.CHESTPLATE, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient && user instanceof ServerPlayerEntity player) {
            ContainerComponent container = stack.getOrDefault(
                    DataComponentTypes.CONTAINER,
                    ContainerComponent.DEFAULT
            );

            DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);
            container.copyTo(items);

            SimpleInventory inventory = new SimpleInventory(items.toArray(new ItemStack[0])) {
                @Override
                public void setStack(int slot, ItemStack newStack) {
                    if (newStack.getItem() instanceof BackpackItem) {
                        return; // Запрещаем ложить рюкзак
                    }
                    super.setStack(slot, newStack);
                }
            };

            // Сохранение при изменениях
            inventory.addListener(inv -> {
                List<ItemStack> updatedStacks = new ArrayList<>();
                for (int i = 0; i < inv.size(); i++) {
                    updatedStacks.add(inv.getStack(i));
                }

                ItemStack newStack = stack.copy();
                newStack.set(
                        DataComponentTypes.CONTAINER,
                        ContainerComponent.fromStacks(updatedStacks)
                );
                user.setStackInHand(hand, newStack);
            });

            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, playerInventory, playerEntity) ->
                            new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, playerInventory, inventory, 3) {
                                @Override
                                protected Slot addSlot(Slot slot) {
                                    // Кастомный слот, который запрещает класть рюкзак
                                    return super.addSlot(new Slot(slot.inventory, slot.getIndex(), slot.x, slot.y) {
                                        @Override
                                        public boolean canInsert(ItemStack stack) {
                                            return !(stack.getItem() instanceof BackpackItem);
                                        }
                                    });
                                }
                            },
                    TITLE
            ));
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
