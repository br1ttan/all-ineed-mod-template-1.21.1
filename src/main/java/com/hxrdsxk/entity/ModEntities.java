package com.hxrdsxk.entity;

import com.hxrdsxk.AllINeedMod;
import com.hxrdsxk.entity.custom.steve.SteveNpcEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.entity.EntityType;


public class ModEntities {
    public static final EntityType<SteveNpcEntity> STEVE_NPC = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(AllINeedMod.MOD_ID, "steve_npc"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SteveNpcEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .build()
    );

    public static void register() {
        net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry.register(
                STEVE_NPC, SteveNpcEntity.createSteveAttributes()
        );
    }
}
