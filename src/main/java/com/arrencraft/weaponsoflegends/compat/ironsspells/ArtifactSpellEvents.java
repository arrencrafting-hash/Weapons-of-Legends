package com.arrencraft.weaponsoflegends.compat.ironsspells;

import com.arrencraft.weaponsoflegends.WeaponsofLegends;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;

@EventBusSubscriber(modid = WeaponsofLegends.MODID)
public class ArtifactSpellEvents {

    @SubscribeEvent
    public static void addArtifactSpells(SpellSelectionManager.SpellSelectionEvent event) {

        ItemStack stack = event.getEntity().getMainHandItem();

        if (stack.getItem() instanceof ArtifactSpellProvider provider) {
            provider.addArtifactSpells(event, stack);
        }
    }
}