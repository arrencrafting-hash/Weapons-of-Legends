package com.arrencraft.weaponsoflegends.mixin;

import com.arrencraft.weaponsoflegends.PairedWeapon;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ItemInHandRenderer.class)

public abstract class ItemInHandRendererMixin {

    @ModifyArgs(
            method = "renderHandsWithItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
            )
    )

    private void weaponsOfLegends$renderPairedWeapon(Args args) {
        AbstractClientPlayer player = args.get(0);
        InteractionHand hand = args.get(3);
        ItemStack stack = args.get(5);

        if (hand == InteractionHand.OFF_HAND
            && stack.isEmpty()
            && player.getMainHandItem().getItem() instanceof PairedWeapon) {

            args.set(5, player.getMainHandItem());
        }
    }
}
