package com.arrencraft.weaponsoflegends.progression.forge;

import com.arrencraft.weaponsoflegends.WeaponsofLegends;
import com.arrencraft.weaponsoflegends.api.ArtifactItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class LegendaryForgeBlockEntity extends BlockEntity {

    public LegendaryForgeBlockEntity(BlockPos pos, BlockState state) {
        super(WeaponsofLegends.LEGENDARY_FORGE_BLOCK_ENTITY.get(), pos, state);
    }

    private final ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof ArtifactItem;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level == null || level.isClientSide) {
            return;
        }

        boolean formed = getBlockState().getValue(LegendaryForgeBlock.FORMED);

        if (formed) {
            level.scheduleTick(
                    getBlockPos(),
                    getBlockState().getBlock(),
                    1
            );
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("inventory")) {
            inventory.deserializeNBT(
                    registries,
                    tag.getCompound("inventory")
            );
        }
    }

}
