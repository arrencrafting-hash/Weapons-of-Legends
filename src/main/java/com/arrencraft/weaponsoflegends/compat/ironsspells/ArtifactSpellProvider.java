package com.arrencraft.weaponsoflegends.compat.ironsspells;

import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import net.minecraft.world.item.ItemStack;

public interface ArtifactSpellProvider {

    void addArtifactSpells(
            SpellSelectionManager.SpellSelectionEvent event,
            ItemStack stack
    );
}