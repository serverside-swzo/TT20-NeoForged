package net.swzo.tt20neoforged.mixin.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.swzo.tt20neoforged.Tt20NeoForged;
import net.swzo.tt20neoforged.config.MainConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Inject(
            method = "getDestroyProgress",
            at = @At("RETURN"),
            cancellable = true
    )
    private void onBlockBreakingCalc(BlockState state, Player player, BlockGetter getter, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (MainConfig.INSTANCE.enabled() && MainConfig.INSTANCE.blockBreakingAcceleration()) {
            if (!player.level().isClientSide()) {

                if (Tt20NeoForged.TPS_CALCULATOR != null) {
                    float original = cir.getReturnValue();
                    cir.setReturnValue(original * 20.0F / (float) Tt20NeoForged.TPS_CALCULATOR.getMostAccurateTPS());
                }
            }
        }
    }
}