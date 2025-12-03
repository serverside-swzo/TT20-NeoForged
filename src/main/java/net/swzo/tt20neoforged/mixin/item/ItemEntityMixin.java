package net.swzo.tt20neoforged.mixin.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.swzo.tt20neoforged.Tt20NeoForged;
import net.swzo.tt20neoforged.config.MainConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    private int pickupDelay;

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void pickupDelayTT20(CallbackInfo ci) {

        if (MainConfig.INSTANCE.enabled() && MainConfig.INSTANCE.pickupAcceleration()) {

            if (!((Entity)(Object)this).level().isClientSide()) {
                if (this.pickupDelay != 0) {

                    if (Tt20NeoForged.TPS_CALCULATOR != null) {
                        int ticksToApply = Tt20NeoForged.TPS_CALCULATOR.applicableMissedTicks();
                        int cap = MainConfig.INSTANCE.getTickRepeatCap();

                        if (cap > 0) {
                            ticksToApply = Math.min(ticksToApply, cap);
                        }

                        if (this.pickupDelay - ticksToApply <= 0) {
                            this.pickupDelay = 0;
                        } else {
                            this.pickupDelay -= ticksToApply;
                        }
                    }
                }
            }
        }
    }
}