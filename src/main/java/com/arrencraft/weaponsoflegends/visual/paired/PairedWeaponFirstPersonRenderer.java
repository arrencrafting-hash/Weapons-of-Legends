package com.arrencraft.weaponsoflegends.visual.paired;

import com.arrencraft.weaponsoflegends.WeaponsofLegends;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import com.arrencraft.weaponsoflegends.api.PairedWeapon;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemDisplayContext;

@EventBusSubscriber(
        modid = WeaponsofLegends.MODID,
        value = Dist.CLIENT
)

public final class PairedWeaponFirstPersonRenderer {

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {

        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        if (!event.getItemStack().isEmpty()) {
            return;
        }

        ItemStack pairedStack;

        if (event.getHand() == InteractionHand.MAIN_HAND) {
            pairedStack = player.getOffhandItem();
        } else {
            pairedStack = player.getMainHandItem();
        }

        if (!(pairedStack.getItem() instanceof PairedWeapon)) {
            return;
        }

        HumanoidArm targetArm;

        if (event.getHand() == InteractionHand.MAIN_HAND) {
            targetArm = player.getMainArm();
        } else {
            targetArm = player.getMainArm().getOpposite();
        }

        boolean lefthand = targetArm == HumanoidArm.LEFT;

        PoseStack poseStack = event.getPoseStack();

        poseStack.pushPose();

        poseStack.translate(
                lefthand ? -0.56F : 0.56F,
                -0.52F,
                -0.72F
        );

        ItemInHandRenderer itemInHandRenderer =
                Minecraft.getInstance()
                                .getEntityRenderDispatcher().getItemInHandRenderer();

        itemInHandRenderer.renderItem(
                player,
                pairedStack,
                lefthand
                        ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                        : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,
                lefthand,
                poseStack,
                event.getMultiBufferSource(),
                event.getPackedLight()

        );

        poseStack.popPose();

        event.setCanceled(true);

    }

}