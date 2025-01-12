package com.alekiponi.firmaciv.common.entity;

import com.alekiponi.alekiships.common.entity.CannonEntity;
import com.alekiponi.alekiships.common.entity.vehicle.RowboatEntity;
import com.alekiponi.alekiships.common.entity.vehicle.SloopEntity;
import com.alekiponi.alekiships.common.entity.vehicle.SloopUnderConstructionEntity;
import com.alekiponi.alekiships.common.entity.vehiclehelper.CompartmentType;
import com.alekiponi.alekiships.common.entity.vehiclehelper.compartment.AbstractCompartmentEntity;
import com.alekiponi.alekiships.util.CommonHelper;
import com.alekiponi.firmaciv.common.entity.compartment.LargeVesselCompartmentEntity;
import com.alekiponi.firmaciv.common.entity.compartment.TFCBarrelCompartmentEntity;
import com.alekiponi.firmaciv.common.entity.compartment.TFCChestCompartmentEntity;
import com.alekiponi.firmaciv.common.entity.vehicle.*;
import com.alekiponi.firmaciv.util.FirmacivTags;
import com.alekiponi.firmaciv.util.TFCWood;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Locale;

import static com.alekiponi.firmaciv.Firmaciv.MOD_ID;

public final class FirmacivEntities {

    private static final int LARGE_VEHICLE_TRACKING = 20;
    private static final int VEHICLE_HELPER_TRACKING = LARGE_VEHICLE_TRACKING + 1;
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(
            ForgeRegistries.ENTITY_TYPES, MOD_ID);

    public static final EnumMap<TFCWood, RegistryObject<EntityType<FirmacivRowboatEntity>>> TFC_ROWBOATS = CommonHelper.mapOfKeys(
            TFCWood.class, tfcWood -> registerRowboat(tfcWood,
                    EntityType.Builder.of((entityType, level) -> new FirmacivRowboatEntity(entityType, level, tfcWood),
                            MobCategory.MISC)));

    public static final EnumMap<TFCWood, RegistryObject<EntityType<FirmacivSloopEntity>>> TFC_SLOOPS = CommonHelper.mapOfKeys(
            TFCWood.class, tfcWood -> registerSloop(tfcWood,
                    EntityType.Builder.of((entityType, level) -> new FirmacivSloopEntity(entityType, level, tfcWood),
                            MobCategory.MISC)));

    public static final EnumMap<TFCWood, RegistryObject<EntityType<FirmacivSloopUnderConstructionEntity>>> TFC_SLOOPS_UNDER_CONSTRUCTION = CommonHelper.mapOfKeys(
            TFCWood.class, tfcWood -> registerSloopConstruction(tfcWood,
                    EntityType.Builder.of(
                            (entityType, level) -> new FirmacivSloopUnderConstructionEntity(entityType, level, tfcWood),
                            MobCategory.MISC)));

    public static final EnumMap<TFCWood, RegistryObject<EntityType<CanoeEntity>>> TFC_CANOES = CommonHelper.mapOfKeys(
            TFCWood.class, tfcWood -> registerCanoe(tfcWood,
                    EntityType.Builder.of(
                            (entityType, level) -> new CanoeEntity(entityType, level, tfcWood),
                            MobCategory.MISC)));

    public static final RegistryObject<EntityType<KayakEntity>> KAYAK_ENTITY = register("kayak",
            EntityType.Builder.of(KayakEntity::new, MobCategory.MISC).sized(0.79F, 0.625F));

    public static final RegistryObject<EntityType<FirmacivCannonEntity>> FIRMACIV_CANNON_ENTITY = register("cannon",
            EntityType.Builder.of(FirmacivCannonEntity::new, MobCategory.MISC).sized(0.8F, 0.8F).fireImmune());

    public static final RegistryObject<CompartmentType<TFCChestCompartmentEntity>> TFC_CHEST_COMPARTMENT_ENTITY = CompartmentType.register(
            registerCompartment("compartment_tfc_chest",
                    CompartmentType.Builder.of(TFCChestCompartmentEntity::new, TFCChestCompartmentEntity::new,
                            MobCategory.MISC)), itemStack -> itemStack.is(FirmacivTags.Items.CHESTS));

    public static final RegistryObject<CompartmentType<TFCBarrelCompartmentEntity>> TFC_BARREL_COMPARTMENT_ENTITY = CompartmentType.register(
            registerCompartment("compartment_tfc_barrel",
                    CompartmentType.Builder.of(TFCBarrelCompartmentEntity::new, TFCBarrelCompartmentEntity::new)),
            itemStack -> itemStack.is(FirmacivTags.Items.BARRELS));

    public static final RegistryObject<CompartmentType<LargeVesselCompartmentEntity>> LARGE_VESSEL_COMPARTMENT_ENTITY = CompartmentType.register(
            registerCompartment("compartment_large_vessel",
                    CompartmentType.Builder.of(LargeVesselCompartmentEntity::new, LargeVesselCompartmentEntity::new)),
            itemStack -> itemStack.is(FirmacivTags.Items.FIRED_LARGE_VESSELS));

    private static <E extends RowboatEntity> RegistryObject<EntityType<E>> registerRowboat(final TFCWood tfcWood,
                                                                                           final EntityType.Builder<E> builder) {
        return register("rowboat/" + tfcWood.getSerializedName(), builder.sized(1.875F, 0.625F));
    }

    private static <E extends CanoeEntity> RegistryObject<EntityType<E>> registerCanoe(final TFCWood tfcWood,
                                                                                       final EntityType.Builder<E> builder) {
        return register("dugout_canoe/" + tfcWood.getSerializedName(), builder.sized(1.125F, 0.625F));
    }

    private static <E extends SloopEntity> RegistryObject<EntityType<E>> registerSloop(final TFCWood tfcWood,
                                                                                       final EntityType.Builder<E> builder) {
        return register("sloop/" + tfcWood.getSerializedName(),
                builder.sized(3F, 0.75F).setTrackingRange(LARGE_VEHICLE_TRACKING).fireImmune());
    }

    private static <E extends SloopUnderConstructionEntity> RegistryObject<EntityType<E>> registerSloopConstruction(
            final TFCWood bopWood, final EntityType.Builder<E> builder) {
        return register("sloop_construction/" + bopWood.getSerializedName(),
                builder.sized(4F, 0.75F).setTrackingRange(LARGE_VEHICLE_TRACKING).fireImmune().noSummon());
    }

    /**
     * Registers a compartment entity
     */
    @SuppressWarnings("SameParameterValue")
    private static <E extends AbstractCompartmentEntity> RegistryObject<CompartmentType<E>> registerCompartment(
            final String name, final CompartmentType.Builder<E> builder) {
        return register(name, builder.sized(0.6F, 0.7F).fireImmune().noSummon(), true);
    }

    /**
     * Base method for registering a compartment entity
     */
    @SuppressWarnings("SameParameterValue")
    private static <E extends AbstractCompartmentEntity> RegistryObject<CompartmentType<E>> register(final String name,
                                                                                                     final CompartmentType.Builder<E> builder, final boolean serialize) {
        final String id = name.toLowerCase(Locale.ROOT);
        return ENTITY_TYPES.register(id, () -> {
            if (!serialize) builder.noSave();
            return builder.build(MOD_ID + ":" + id);
        });
    }

    private static <E extends Entity> RegistryObject<EntityType<E>> register(final String name,
                                                                             final EntityType.Builder<E> builder) {
        return register(name, builder, true);
    }

    @SuppressWarnings("SameParameterValue")
    private static <E extends Entity> RegistryObject<EntityType<E>> register(final String name,
                                                                             final EntityType.Builder<E> builder, final boolean serialize) {
        final String id = name.toLowerCase(Locale.ROOT);
        return ENTITY_TYPES.register(id, () -> {
            if (!serialize) builder.noSave();
            return builder.build(MOD_ID + ":" + id);
        });
    }
}