package com.hyperdash.firmaciv.entity;

import com.hyperdash.firmaciv.FirmaCiv;
import com.hyperdash.firmaciv.entity.custom.CanoeEntity;
import com.hyperdash.firmaciv.entity.custom.FirmacivBoatEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FirmacivEntities {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES
            = DeferredRegister.create(ForgeRegistries.ENTITIES, FirmaCiv.MOD_ID);

    public static final RegistryObject<EntityType<CanoeEntity>> CANOE_ENTITY = ENTITY_TYPES.register("canoe_entity",
            () -> EntityType.Builder.of(CanoeEntity::new, MobCategory.MISC).sized(1F, 0.5625F)
                    .build(new ResourceLocation(FirmaCiv.MOD_ID, "canoe_entity").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    public static class ResLocation {
        public static final ResourceLocation CANOE_ENTITY = new ResourceLocation(FirmaCiv.MOD_ID, "canoe_entity");
    }


}
