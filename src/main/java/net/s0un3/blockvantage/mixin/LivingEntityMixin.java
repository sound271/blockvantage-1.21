package net.s0un3.blockvantage.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
    private void onDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!((LivingEntity)(Object)this instanceof PlayerEntity player)) return;
        onShieldBlocked(player);
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"))
    private void onPush(Entity entity, CallbackInfo ci) {
        LivingEntity entityPushed = (LivingEntity)(Object)this;
        if(entityPushed instanceof PlayerEntity) return;
        if(!(entity instanceof PlayerEntity player)) return;
        if (player.getStatusEffects().stream().noneMatch(effect -> effect.getEffectType() == StatusEffects.STRENGTH && effect.getAmplifier() == 4)) return;
        if(entityPushed.getWorld().isClient) return;
        DamageSource damageSource = new DamageSource(
                entityPushed.getWorld().getRegistryManager()
                        .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                        .getEntry(DamageTypes.GENERIC.getValue()).get()
        );
        entityPushed.damage((ServerWorld) entityPushed.getWorld(), damageSource, 5.0F);
        entityPushed.setVelocity(player.getMovementDirection().getDoubleVector().multiply(3).add(0, 1, 0));
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


