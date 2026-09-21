package com.arrencraft.weaponsoflegends.visual.paired;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import com.arrencraft.weaponsoflegends.api.PairedWeapon;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Axis;

public class PairedWeaponRenderLayer
        extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final ItemInHandRenderer itemInHandRenderer;

    public PairedWeaponRenderLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
            ItemInHandRenderer itemInHandRenderer
    ) {
        super(parent);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {

        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();

        boolean pairedInMain =
                mainHandStack.getItem() instanceof PairedWeapon;

        boolean pairedInOff =
                offHandStack.getItem() instanceof PairedWeapon;

        ItemStack pairedStack;
        HumanoidArm targetArm;

        if (pairedInMain && offHandStack.isEmpty()) {
            pairedStack = mainHandStack;
            targetArm = player.getMainArm().getOpposite();
        } else if (pairedInOff && mainHandStack.isEmpty()) {
            pairedStack = offHandStack;
            targetArm = player.getMainArm();
        } else {
            return;
        }

        boolean leftHand = targetArm == HumanoidArm.LEFT;

        poseStack.pushPose();

        getParentModel().translateToHand(targetArm, poseStack);

        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        poseStack.translate(
                leftHand ? -1.0F / 16.0F : 1.0F / 16.0F,
                0.125F,
                -0.625F
        );

        itemInHandRenderer.renderItem(
                player,
                pairedStack,
                leftHand
                        ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                        : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                leftHand,
                poseStack,
                bufferSource,
                packedLight
        );

        poseStack.popPose();

    }
}