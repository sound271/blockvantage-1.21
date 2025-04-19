package net.s0un3.blockvantage.Item;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;

public class ModItems {

    public static void onShieldBlocked(PlayerEntity player) {
        ApplyEffect(player, StatusEffects.RESISTANCE, 60, 4);
        ApplyEffect(player, StatusEffects.STRENGTH, 60, 4);
        ApplyEffect(player, StatusEffects.SPEED, 40, 1);
    }

    public static void ApplyEffect(PlayerEntity player, RegistryEntry<StatusEffect> effect,int Time ,int Amplifier)
    {
        player.addStatusEffect(new StatusEffectInstance(effect, Time, Amplifier));
    }
}