package net.swzo.tt20neoforged.mixin.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.swzo.tt20neoforged.util.TPSUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void onGetUseDuration(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        int original = cir.getReturnValue();
        if (original == 0) return;
        int modified = TPSUtil.tt20(original, true);
        if (modified != original) cir.setReturnValue(modified);
    }
}