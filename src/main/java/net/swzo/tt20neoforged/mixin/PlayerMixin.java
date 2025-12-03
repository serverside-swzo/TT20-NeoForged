package net.swzo.tt20neoforged.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.swzo.tt20neoforged.Tt20NeoForged;
import net.swzo.tt20neoforged.config.MainConfig;
import net.swzo.tt20neoforged.util.TPSUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Shadow public abstract int getSleepTimer();

    @Inject(
            method = "getDimensionChangingDelay",
            at = @At("RETURN"),
            cancellable = true
    )
    private void netherPortalTimeTT20(CallbackInfoReturnable<Integer> cir) {
        int original = cir.getReturnValue();
        if (MainConfig.INSTANCE.enabled() && MainConfig.INSTANCE.portalAcceleration()) {

            if (!((Entity) (Object) this).level().isClientSide()) {
                if (original != 1) {
                    cir.setReturnValue(TPSUtil.tt20(original, false));
                }
            }
        }
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/player/Player;sleepCounter:I",
                    opcode = 180 
            )
    )
    private int tickTT20(Player player) {
        int original = player.getSleepTimer();
        if (MainConfig.INSTANCE.enabled() && MainConfig.INSTANCE.sleepingAcceleration()) {

            if (((Entity) (Object) this).level().isClientSide()) {
                return original;
            } else {
                if (Tt20NeoForged.TPS_CALCULATOR != null) {
                    int ticksToApply = Tt20NeoForged.TPS_CALCULATOR.applicableMissedTicks();
                    int cap = MainConfig.INSTANCE.getTickRepeatCap();

                    if (cap > 0) {
                        ticksToApply = Math.min(ticksToApply, cap);
                    }

                    return player.isSleeping() ? original + ticksToApply : original;
                }
                return original;
            }
        } else {
            return original;
        }
    }
}