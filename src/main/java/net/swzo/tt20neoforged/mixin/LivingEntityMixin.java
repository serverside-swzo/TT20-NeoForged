package net.swzo.tt20neoforged.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.swzo.tt20neoforged.Tt20NeoForged;
import net.swzo.tt20neoforged.config.MainConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    protected abstract void tickEffects();

    @Inject(
            method = "baseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;tickEffects()V"
            )
    )
    private void fixPotionDelayTick(CallbackInfo ci) {
        if (MainConfig.INSTANCE.enabled() && MainConfig.INSTANCE.potionEffectAcceleration()) {

            if (!((Entity)(Object)this).level().isClientSide()) {

                if (Tt20NeoForged.TPS_CALCULATOR != null) {
                    int ticksToApply = Tt20NeoForged.TPS_CALCULATOR.applicableMissedTicks();
                    int cap = MainConfig.INSTANCE.getTickRepeatCap();

                    if (cap > 0) {
                        ticksToApply = Math.min(ticksToApply, cap);
                    }

                    for (int i = 0; i < ticksToApply; ++i) {
                        this.tickEffects();
                    }
                }
            }
        }
    }
}