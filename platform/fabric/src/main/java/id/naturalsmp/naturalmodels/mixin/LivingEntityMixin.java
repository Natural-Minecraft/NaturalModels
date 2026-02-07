/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import id.naturalsmp.naturalmodels.impl.fabric.events.ServerLivingEntityJumpCallback;
import id.naturalsmp.naturalmodels.impl.fabric.events.ServerMobEffectLoadCallback;
import id.naturalsmp.naturalmodels.impl.fabric.events.ServerMobEffectUnloadCallback;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;jumpFromGround()V"
        )
    )
    private void NaturalModels$invokeJumpCallbacks(@NotNull CallbackInfo ci) {
        if (level().isClientSide()) return;

        ServerLivingEntityJumpCallback.EVENT.invoker().onJump(NaturalModels$livingEntity());
    }

    @Inject(
        method = "onEffectAdded",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/effect/MobEffect;addAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V",
            shift = At.Shift.AFTER
        )
    )
    private void NaturalModels$invokeEffectLoadCallbacks(@NotNull MobEffectInstance effect, @NotNull Entity source, @NotNull CallbackInfo ci) {
        if (level().isClientSide()) return;

        ServerMobEffectLoadCallback.EVENT.invoker().onLoad(NaturalModels$livingEntity(), effect);
    }

    @Inject(
        method = "onEffectsRemoved",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;)V",
            shift = At.Shift.AFTER
        )
    )
    private void NaturalModels$invokeEffectUnloadCallbacks(@NotNull Collection<MobEffectInstance> effects, @NotNull CallbackInfo ci, @Local @NotNull MobEffectInstance effect) {
        if (level().isClientSide()) return;

        ServerMobEffectUnloadCallback.EVENT.invoker().onUnload(NaturalModels$livingEntity(), effect);
    }

    @Unique
    private @NotNull LivingEntity NaturalModels$livingEntity() {
        return (LivingEntity) (Object) this;
    }
}

