package com.arrencraft.weaponsoflegends.api;

import net.minecraft.world.item.Item;

public class ArtifactItem extends Item {

    public ArtifactItem(Properties properties) {
        super(properties.stacksTo(1));
    }
}