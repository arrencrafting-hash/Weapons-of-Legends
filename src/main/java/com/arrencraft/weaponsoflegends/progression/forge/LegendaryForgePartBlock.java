package com.arrencraft.weaponsoflegends.progression.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import com.arrencraft.weaponsoflegends.WeaponsofLegends;

public class LegendaryForgePartBlock extends Block {

    public LegendaryForgePartBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {

        if (!state.is(newState.getBlock()) && !level.isClientSide) {
            notifyForgeController(level, pos);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }


    private void notifyForgeController(Level level, BlockPos pos) {

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {

                BlockPos checkPos = pos.offset(x, 0, z);

                if (level.getBlockState(checkPos)
                        .is(WeaponsofLegends.LEGENDARY_FORGE.get())) {

                    level.scheduleTick(
                            checkPos,
                            WeaponsofLegends.LEGENDARY_FORGE.get(),
                            1
                    );

                    return;

                }
            }
        }

    }

}

