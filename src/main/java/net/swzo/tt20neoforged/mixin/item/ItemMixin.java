package net.swzo.tt20neoforged.mixin.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.swzo.tt20neoforged.config.MainConfig;
import net.swzo.tt20neoforged.util.TPSUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(
            method = "getUseDuration",
            at = @At("RETURN"),
            cancellable = true
    )
    private void onGetMaxUseTime(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {

        Integer retVal = cir.getReturnValue();
        int original = (retVal != null) ? retVal : 0;

        if (MainConfig.INSTANCE.enabled() && MainConfig.INSTANCE.eatingAcceleration() && original != 0) {
            cir.setReturnValue(TPSUtil.tt20(original, true));
        }
    }
}