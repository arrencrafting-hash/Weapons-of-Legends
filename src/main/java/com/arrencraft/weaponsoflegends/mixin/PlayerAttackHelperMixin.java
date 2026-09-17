package com.arrencraft.weaponsoflegends.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.arrencraft.weaponsoflegends.PairedWeapon;
import net.bettercombat.logic.PlayerAttackHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerAttackHelper.class)
public abstract class PlayerAttackHelperMixin {

    private static ItemStack weaponsOfLegends$getVirtualOffhand(Player player) {

        ItemStack offhand = player.getOffhandItem();

        if (offhand.isEmpty()
                && player.getMainHandItem().getItem() instanceof PairedWeapon) {
            return player.getMainHandItem();
        }

        return offhand;
    }

    @Redirect(
            method = {
                    "isDualWielding(Lnet/minecraft/world/entity/player/Player;)Z",
                    "getCurrentAttack",
                    "evaluateCondition",
                    "poseForPlayer"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getOffhandItem()Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private static ItemStack weaponsOfLegends$redirectOffhand(Player player) {
        return weaponsOfLegends$getVirtualOffhand(player);
    }
}