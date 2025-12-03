package net.swzo.tt20neoforged.mixin.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.WritableLevelData;
import net.swzo.tt20neoforged.Tt20NeoForged;
import net.swzo.tt20neoforged.config.MainConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Redirect(
            method = "tickTime",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/WritableLevelData;getDayTime()J"
            )
    )
    private long addMissingTicksToTime(WritableLevelData instance) {
        long original = instance.getDayTime();
        if (MainConfig.INSTANCE.enabled() && MainConfig.INSTANCE.timeAcceleration()) {
            if (Tt20NeoForged.TPS_CALCULATOR != null) {
                int ticksToApply = Tt20NeoForged.TPS_CALCULATOR.applicableMissedTicks();
                int cap = MainConfig.INSTANCE.getTickRepeatCap();

                if (cap > 0) {
                    ticksToApply = Math.min(ticksToApply, cap);
                }

                return original + (long) ticksToApply;
            }
            return original;
        } else {
            return original;
        }
    }
}