/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.bukkit.platform;

import id.naturalsmp.naturalmodels.api.bukkit.NaturalModelsBukkit;
import id.naturalsmp.naturalmodels.api.platform.PlatformLocation;
import id.naturalsmp.naturalmodels.api.platform.PlatformWorld;
import id.naturalsmp.naturalmodels.api.scheduler.ModelTask;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Bukkit location wrapped as a {@link PlatformLocation}.
 *
 * @param source the source Bukkit location
 * @since 2.0.0
 */
public record BukkitLocation(@NotNull Location source) implements PlatformLocation {

    @Override
    public @NotNull PlatformWorld world() {
        return BukkitAdapter.adapt(source.getWorld());
    }

    @Override
    public double x() {
        return source.getX();
    }

    @Override
    public double y() {
        return source.getY();
    }

    @Override
    public double z() {
        return source.getZ();
    }

    @Override
    public float pitch() {
        return source.getPitch();
    }

    @Override
    public float yaw() {
        return source.getYaw();
    }

    @Override
    public @NotNull PlatformLocation add(double x, double y, double z) {
        return BukkitAdapter.adapt(source.clone().add(x, y, z));
    }

    @Override
    public @Nullable ModelTask task(@NotNull Runnable runnable) {
        return NaturalModelsBukkit.platform().scheduler().task(source, runnable);
    }

    @Override
    public @Nullable ModelTask taskLater(long delay, @NotNull Runnable runnable) {
        return NaturalModelsBukkit.platform().scheduler().taskLater(source, delay, runnable);
    }
}

