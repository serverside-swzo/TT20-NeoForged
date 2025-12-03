package net.swzo.tt20neoforged.mixin.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.swzo.tt20neoforged.Tt20NeoForged;
import net.swzo.tt20neoforged.config.BlockEntityMaskConfig;
import net.swzo.tt20neoforged.config.MainConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.level.chunk.LevelChunk$BoundTickingBlockEntity")
public abstract class LevelChunk$BoundTickingBlockEntityMixin {

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntityTicker;tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;)V"
            )
    )
    private <T extends BlockEntity> void onTick(BlockEntityTicker<T> instance, Level level, BlockPos blockPos, BlockState blockState, T t) {

        instance.tick(level, blockPos, blockState, t);

        if (MainConfig.INSTANCE.enabled()) {
            if (MainConfig.INSTANCE.blockEntityAcceleration()) {
                if (!level.isClientSide()) {

                    if (BlockEntityMaskConfig.INSTANCE.getMask().isOkay(BuiltInRegistries.BLOCK.getKey(blockState.getBlock()))) {

                        if (Tt20NeoForged.TPS_CALCULATOR != null) {
                            int ticksToApply = Tt20NeoForged.TPS_CALCULATOR.applicableMissedTicks();
                            int cap = MainConfig.INSTANCE.getTickRepeatCap();

                            if (cap > 0) {
                                ticksToApply = Math.min(ticksToApply, cap);
                            }

                            for (int i = 0; i < ticksToApply; ++i) {
                                instance.tick(level, blockPos, blockState, t);
                            }
                        }
                    }
                }
            }
        }
    }
}