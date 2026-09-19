package com.arrencraft.weaponsoflegends.mixin;

import com.arrencraft.weaponsoflegends.api.PairedWeapon;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin {

    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/layers/ItemInHandLayer;renderArmWithItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
            )
    )
    private void weaponsOfLegends$renderPairedWeapon(Args args) {
        LivingEntity entity = args.get(0);
        ItemStack stack = args.get(1);
        HumanoidArm arm = args.get(3);

        ItemStack mainHandStack = entity.getMainHandItem();

        if (arm != entity.getMainArm()
                && stack.isEmpty()
                && mainHandStack.getItem() instanceof PairedWeapon) {

            args.set(1, mainHandStack);
        }
    }
}