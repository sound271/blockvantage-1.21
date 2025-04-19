package net.s0un3.blockvantage.mixin;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.s0un3.blockvantage.Item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (entity instanceof PlayerEntity player && player.isBlocking()) {
            if (player.getActiveItem().getItem() instanceof ShieldItem) {
                ModItems.onShieldBlocked(player);
            }
        }
    }
}


