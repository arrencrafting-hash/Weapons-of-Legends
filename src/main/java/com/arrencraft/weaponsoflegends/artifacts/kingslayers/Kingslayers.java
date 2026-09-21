package com.arrencraft.weaponsoflegends.artifacts.kingslayers;

import com.arrencraft.weaponsoflegends.api.ArtifactWeapon;
import com.arrencraft.weaponsoflegends.api.PairedWeapon;
import com.arrencraft.weaponsoflegends.artifacts.kingslayers.client.KingslayersRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Kingslayers extends ArtifactWeapon implements GeoItem, PairedWeapon {
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

}