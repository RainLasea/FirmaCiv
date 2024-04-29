package com.alekiponi.firmaciv.events;

import com.alekiponi.firmaciv.Firmaciv;
import com.alekiponi.firmaciv.common.block.CanoeComponentBlock;
import com.alekiponi.firmaciv.common.blockentity.CanoeComponentBlockEntity;
import com.alekiponi.firmaciv.events.config.FirmacivConfig;
import com.alekiponi.firmaciv.util.FirmacivTags;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.alekiponi.firmaciv.common.block.CanoeComponentBlock.*;

@Mod.EventBusSubscriber(modid = Firmaciv.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BlockEventHandler {

    private BlockEventHandler() {
    }

    @SubscribeEvent
    public static void registerFireStarterEvents(StartFireEvent event) {

        if (event.getState().is(FirmacivTags.Blocks.CANOE_COMPONENT_BLOCKS)) {

            if (event.getState().getValue(CANOE_CARVED) == 11) {

                BlockEntity blockEntity = event.getPlayer().level().getBlockEntity(event.getPos());

                if (blockEntity instanceof CanoeComponentBlockEntity ccBlockEntity) {
                    ccBlockEntity.light();
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerToolModificationEvents(BlockEvent.BlockToolModificationEvent event) {

        if (event.getToolAction() == ToolActions.AXE_STRIP && (event.getState()
                .is(FirmacivTags.Blocks.CAN_MAKE_CANOE) || (!FirmacivConfig.SERVER.canoeWoodRestriction.get() && event.getState()
                .is(FirmacivTags.Blocks.CAN_MAKE_CANOE_UNRESTRICTED))) &&
                event.getPlayer().getItemInHand(event.getPlayer().getUsedItemHand()).is(FirmacivTags.Items.SAWS)) {
            if (event.getState().getValue(BlockStateProperties.AXIS).isHorizontal()) {
                convertLogToCanoeComponent(event);
            }
        }

        if (event.getToolAction() == ToolActions.AXE_STRIP && (event.getState()
                .is(FirmacivTags.Blocks.CANOE_COMPONENT_BLOCKS))) {
            if (event.getState().getValue(BlockStateProperties.AXIS).isHorizontal()) {
                processCanoeComponent(event);
            }
        }

    }

    // TODO this can be much cleaner
    private static void processCanoeComponent(BlockEvent.BlockToolModificationEvent event) {

        Block canoeComponentBlock = event.getState().getBlock();
        BlockState canoeComponentBlockState = event.getState();
        BlockPos thisBlockPos = event.getPos();
        LevelAccessor world = event.getLevel();
        Direction.Axis axis = event.getState().getValue(AXIS);

        int nextCanoeCarvedState = event.getState().getValue(CANOE_CARVED) + 1;

        if (canoeComponentBlockState.getValue(CANOE_CARVED) < 5 &&
                event.getPlayer().getItemInHand(event.getPlayer().getUsedItemHand()).is(FirmacivTags.Items.SAWS)) {
            event.getPlayer().swing(event.getPlayer().getUsedItemHand());
            world.setBlock(thisBlockPos, event.getState().setValue(CANOE_CARVED, nextCanoeCarvedState), 2);
            event.getPlayer().level().addDestroyBlockEffect(thisBlockPos, canoeComponentBlockState);
            event.getPlayer().getItemInHand(event.getContext().getHand()).getUseAnimation();
            world.playSound(event.getPlayer(), thisBlockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (nextCanoeCarvedState == 5) {
                Block.dropResources(canoeComponentBlockState, world, thisBlockPos.above(), null);
                //Block.dropResources(canoeComponentBlockState, event.getPlayer().getLevel(), thisBlockPos, null, event.getPlayer(), Item);

            }

        } else if (canoeComponentBlockState.getValue(CANOE_CARVED) >= 5 && canoeComponentBlockState.getValue(
                CANOE_CARVED) < 11 &&
                event.getPlayer().getItemInHand(event.getPlayer().getUsedItemHand()).is(FirmacivTags.Items.AXES)) {
            event.getPlayer().swing(event.getPlayer().getUsedItemHand());
            BlockPos blockPos1 = thisBlockPos.relative(axis, -2);

            // if there are three in a row then it's valid

            boolean flag = false;
            int row = 0;
            for (int i = -2; i <= 2; ++i) {
                blockPos1 = thisBlockPos.relative(axis, i);
                if ((world.getBlockState(blockPos1).is(canoeComponentBlock) && world.getBlockState(blockPos1)
                        .getValue(CANOE_CARVED) >= 5)) {
                    row++;
                    flag = row >= 3;
                } else {
                    row = 0;
                }
            }

            if (flag) {
                world.setBlock(thisBlockPos, event.getState().setValue(CANOE_CARVED, nextCanoeCarvedState), 2);
                event.getPlayer().level().addDestroyBlockEffect(thisBlockPos, canoeComponentBlockState);
                event.getPlayer().getItemInHand(event.getContext().getHand()).getUseAnimation();
                world.playSound(event.getPlayer(), thisBlockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

        }

    }

    // TODO this can be much cleaner
    private static void convertLogToCanoeComponent(BlockEvent.BlockToolModificationEvent event) {
        if (!(event.getState().getBlock() instanceof RotatedPillarBlock strippedLogBlock)) return;

        BlockPos thisBlockPos = event.getPos();
        LevelAccessor world = event.getLevel();
        Level level = event.getPlayer().level();

        if (CanoeComponentBlock.isValidCanoeShape(world, strippedLogBlock, thisBlockPos)) {
            world.playSound(event.getPlayer(), thisBlockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            event.getPlayer().swing(event.getPlayer().getUsedItemHand());
            event.getPlayer().level().addDestroyBlockEffect(thisBlockPos, event.getState());

            final CanoeComponentBlock canoeComponentBlock = getCanoeComponent(strippedLogBlock);

            if (canoeComponentBlock == null) {
                Firmaciv.LOGGER.warn("{} does not map to a canoe component block!",strippedLogBlock);
                return;
            }

            canoeComponentBlock.defaultBlockState().setValue(AXIS, Direction.Axis.Z);
            Direction.Axis axis = event.getState().getValue(AXIS);

            world.setBlock(thisBlockPos,
                    CanoeComponentBlock.getStateForPlacement(level, strippedLogBlock, thisBlockPos), 2);

            BlockPos blockPos1 = thisBlockPos.relative(axis, 1);
            BlockPos blockPos2 = thisBlockPos.relative(axis, -1);

            if (world.getBlockState(blockPos1).is(canoeComponentBlock) && world.getBlockState(blockPos2)
                    .is(canoeComponentBlock)) {
                CanoeComponentBlock.setEndPieces(event.getPlayer().level(), thisBlockPos, canoeComponentBlock, true);
                CanoeComponentBlock.setEndPieces(event.getPlayer().level(), thisBlockPos.relative(axis, -1),
                        canoeComponentBlock, false);
            } else if (level.getBlockState(blockPos1).is(canoeComponentBlock)) {
                setEndPieces(level, blockPos1, canoeComponentBlock, true);
            } else {
                setEndPieces(level, blockPos2, canoeComponentBlock, false);
            }

        }

    }

}
