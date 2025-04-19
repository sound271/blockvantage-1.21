package net.s0un3.blockvantage.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (!(entity instanceof PlayerEntity player)) return;
        if (!player.isBlocking()) return;
        if (player.getActiveItem().getItem() instanceof ShieldItem) {
            onShieldBlocked(player);
        }
    }

    @Unique
    private static void onShieldBlocked(PlayerEntity player) {
        applyEffect(player, StatusEffects.RESISTANCE, 60, 4);
        applyEffect(player, StatusEffects.STRENGTH, 60, 4);
        applyEffect(player, StatusEffects.SPEED, 40, 1);
    }

    @Unique
    private static void applyEffect(PlayerEntity player, RegistryEntry<StatusEffect> effect, int Time, int Amplifier)
    {
        player.addStatusEffect(new StatusEffectInstance(effect, Time, Amplifier));
    }
}


