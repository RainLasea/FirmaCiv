package com.hyperdash.firmaciv.block;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FirmacivBlockStateProperties {


    public static final IntegerProperty CANOE_CARVED_1;
    // does nothing to the appearance of the block
    public static final IntegerProperty CANOE_CARVED_2;
    public static final IntegerProperty CANOE_CARVED_3;
    public static final IntegerProperty CANOE_CARVED_4;
    public static final IntegerProperty CANOE_CARVED_5;
    // stops reducing the height of the block at this stage
    public static final IntegerProperty CANOE_CARVED_6;
    public static final IntegerProperty CANOE_CARVED_7;
    public static final IntegerProperty CANOE_CARVED_8;

    public static final IntegerProperty CANOE_CARVED_9;

    public static final IntegerProperty CANOE_CARVED_10;
    public static final IntegerProperty CANOE_CARVED_11;

    public static final BooleanProperty CANOE_HOLLOWED;
    public static final BooleanProperty CANOE_LIT;

    private static final IntegerProperty[] CANOE_CARVED;

    public static IntegerProperty getCanoeCarvedProperty(int maxStage) {
        if (maxStage > 0 && maxStage <= CANOE_CARVED.length) {
            return CANOE_CARVED[maxStage - 1];
        } else {
            throw new IllegalArgumentException("No canoe_carved property for stages [0, " + maxStage + "]");
        }
    }

    static{
        CANOE_CARVED_1 = IntegerProperty.create("canoe_carved", 0, 1);
        CANOE_CARVED_2 = IntegerProperty.create("canoe_carved", 0, 2);
        CANOE_CARVED_3 = IntegerProperty.create("canoe_carved", 0, 3);
        CANOE_CARVED_4 = IntegerProperty.create("canoe_carved", 0, 4);
        CANOE_CARVED_5 = IntegerProperty.create("canoe_carved", 0, 5);
        CANOE_CARVED_6 = IntegerProperty.create("canoe_carved", 0, 6);
        CANOE_CARVED_7 = IntegerProperty.create("canoe_carved", 0, 7);
        CANOE_CARVED_8 = IntegerProperty.create("canoe_carved", 0, 8);
        CANOE_CARVED_9 = IntegerProperty.create("canoe_carved", 0, 9);
        CANOE_CARVED_10 = IntegerProperty.create("canoe_carved", 0, 10);
        CANOE_CARVED_11 = IntegerProperty.create("canoe_carved", 0, 11);

        CANOE_HOLLOWED = BooleanProperty.create("canoe_hollowed");
        CANOE_LIT = BooleanProperty.create("canoe_lit");

        CANOE_CARVED = new IntegerProperty[]{ CANOE_CARVED_1, CANOE_CARVED_2, CANOE_CARVED_3, CANOE_CARVED_4, CANOE_CARVED_5,
                CANOE_CARVED_6, CANOE_CARVED_7, CANOE_CARVED_8, CANOE_CARVED_9, CANOE_CARVED_10, CANOE_CARVED_11};
    }

}
