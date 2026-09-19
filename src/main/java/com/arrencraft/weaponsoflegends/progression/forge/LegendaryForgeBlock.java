package com.arrencraft.weaponsoflegends.progression.forge;

import com.arrencraft.weaponsoflegends.WeaponsofLegends;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import com.arrencraft.weaponsoflegends.api.ArtifactItem;


public class LegendaryForgeBlock extends Block implements EntityBlock {

    public static final BooleanProperty FORMED = BooleanProperty.create("formed");

    public LegendaryForgeBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any().setValue(FORMED, false)
        );
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);

        if (!state.getValue(FORMED)) {
            return;
        }

        if (!isStructureValid(level, pos)) {

            if (level.getBlockEntity(pos) instanceof LegendaryForgeBlockEntity forgeEntity) {

                ItemStack extracted = forgeEntity.getInventory().extractItem(
                        0,
                        1,
                        false
                );

                if (!extracted.isEmpty()) {
                    popResource(level, pos, extracted);
                }

            }

            level.setBlock(
                    pos,
                    state.setValue(FORMED, false),
                    3
            );

        }

    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LegendaryForgeBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(FORMED);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {

        if (!state.is(newState.getBlock())) {

            if (!level.isClientSide
                    && level.getBlockEntity(pos) instanceof LegendaryForgeBlockEntity forgeEntity) {

                ItemStack extracted = forgeEntity.getInventory().extractItem(
                        0,
                        1,
                        false
                );

                if (!extracted.isEmpty()) {

                    popResource(level, pos, extracted);

                }

            }

        }

        super.onRemove(state, level, pos, newState, movedByPiston);

    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {

        if (stack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!(stack.getItem() instanceof ArtifactItem)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!state.getValue(FORMED)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof LegendaryForgeBlockEntity forge) {

            ItemStack simulated = forge.getInventory().insertItem(
                    0,
                    stack.copy(),
                    true
            );

            if (!simulated.isEmpty()) {
                player.sendSystemMessage(
                        Component.literal("Forge occupied!")
                );

                return ItemInteractionResult.SUCCESS;

            }

            ItemStack remainder = forge.getInventory().insertItem(
                    0,
                    stack.copy(),
                    false
            );

            if (remainder.isEmpty()) {
                stack.shrink(1);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (state.getValue(FORMED)) {
            if (level.getBlockEntity(pos) instanceof LegendaryForgeBlockEntity forgeEntity) {

                ItemStack extracted = forgeEntity.getInventory().extractItem(
                        0,
                        1,
                        false
                );

                if (!extracted.isEmpty()) {
                    if (!player.getInventory().add(extracted)) {
                        player.drop(extracted, false);
                    }
                }
            }

            return InteractionResult.SUCCESS;
        }

        if (isStructureValid(level, pos)) {
            level.setBlock(
                    pos,
                    state.setValue(FORMED, true),
                    3
            );

            player.sendSystemMessage(
                    Component.literal("Structure valid!")
            );
        } else {
            player.sendSystemMessage(
                    Component.literal("Structure incomplete!")
            );
        }

        return InteractionResult.SUCCESS;
    }

    private boolean isStructureValid(Level level, BlockPos pos) {
        return matchesStructure(level, pos, Direction.NORTH)
                || matchesStructure(level, pos, Direction.EAST)
                || matchesStructure(level, pos, Direction.SOUTH)
                || matchesStructure(level, pos, Direction.WEST);
    }

    private boolean matchesStructure(Level level, BlockPos pos, Direction back) {
        Direction left = back.getClockWise();
        Direction right = back.getCounterClockWise();

        return level.getBlockState(pos.relative(left)).is(WeaponsofLegends.LEGENDARY_FORGE_PART)
                && level.getBlockState(pos.relative(right)).is(WeaponsofLegends.LEGENDARY_FORGE_PART)
                && level.getBlockState(pos.relative(back).relative(left)).is(WeaponsofLegends.LEGENDARY_FORGE_PART)
                && level.getBlockState(pos.relative(back)).is(WeaponsofLegends.LEGENDARY_FORGE_PART)
                && level.getBlockState(pos.relative(back).relative(right)).is(WeaponsofLegends.LEGENDARY_FORGE_PART);
    }

}