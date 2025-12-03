package net.swzo.tt20neoforged.mixin.fluid;

import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.WaterFluid;
import net.swzo.tt20neoforged.config.MainConfig;
import net.swzo.tt20neoforged.util.TPSUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin {
    @Inject(
            method = "getTickDelay",
            at = @At("RETURN"),
            cancellable = true
    )
    private void tickRateTT20(LevelReader level, CallbackInfoReturnable<Integer> cir) {
        if (MainConfig.INSTANCE.enabled() && MainConfig.INSTANCE.fluidAcceleration()) {
            int original = cir.getReturnValue();
            cir.setReturnValue(TPSUtil.tt20(original, true));
        }
    }
}