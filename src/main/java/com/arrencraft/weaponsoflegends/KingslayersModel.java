package com.arrencraft.weaponsoflegends;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KingslayersModel extends GeoModel<Kingslayers> {
    @Override
    public ResourceLocation getModelResource(Kingslayers animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                "weapons_of_legends",
                "geo/kingslayers.geo.json"
        );
    }

    @Override
    public ResourceLocation getTextureResource(Kingslayers animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                "weapons_of_legends",
                "textures/kingslayers.png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(Kingslayers animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                "weapons_of_legends",
                "animations/kingslayers.animation.json"
        );
    }
}
