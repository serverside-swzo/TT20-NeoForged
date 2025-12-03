package net.swzo.tt20neoforged.mixin.world;

import net.minecraft.world.level.GameRules;
import net.swzo.tt20neoforged.Tt20NeoForged;
import net.swzo.tt20neoforged.config.MainConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRules.class)
public class GameRulesMixin {
    @Inject(
            method = "getInt",
            at = @At("RETURN"),
            cancellable = true
    )
    private void randomTickSpeedAcceleration(GameRules.Key<GameRules.IntegerValue> rule, CallbackInfoReturnable<Integer> cir) {
        if (MainConfig.INSTANCE.enabled() && MainConfig.INSTANCE.randomTickSpeedAcceleration()) {
            if (rule == GameRules.RULE_RANDOMTICKING) {
                if (Tt20NeoForged.TPS_CALCULATOR != null) {
                    int original = cir.getReturnValue();
                    cir.setReturnValue((int) ((float) (original * 20) / (float) Tt20NeoForged.TPS_CALCULATOR.getMostAccurateTPS()));
                }
            }
        }
    }
}