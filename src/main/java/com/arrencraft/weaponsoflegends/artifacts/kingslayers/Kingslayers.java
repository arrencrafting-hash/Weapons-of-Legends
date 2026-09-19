package com.arrencraft.weaponsoflegends.artifacts.kingslayers;

import com.arrencraft.weaponsoflegends.compat.ironsspells.ArtifactSpellProvider;
import com.arrencraft.weaponsoflegends.artifacts.kingslayers.client.KingslayersRenderer;
import com.arrencraft.weaponsoflegends.api.ArtifactWeapon;
import com.arrencraft.weaponsoflegends.api.PairedWeapon;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Kingslayers extends ArtifactWeapon implements GeoItem, PairedWeapon, ArtifactSpellProvider {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public Kingslayers(Properties properties){
        super(properties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return new KingslayersRenderer();
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void addArtifactSpells(SpellSelectionManager.SpellSelectionEvent event, ItemStack stack) {

        event.addSelectionOption(
                new SpellData(SpellRegistry.INVISIBILITY_SPELL.get(), 1),
                "kingslayers",
                0
        );

    }
}