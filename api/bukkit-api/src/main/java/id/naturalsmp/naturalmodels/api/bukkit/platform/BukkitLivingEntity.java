/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.bukkit.platform;

import id.naturalsmp.naturalmodels.api.platform.PlatformLivingEntity;
import id.naturalsmp.naturalmodels.api.platform.PlatformLocation;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Bukkit living entity wrapped as a {@link PlatformLivingEntity}.
 *
 * @since 2.0.0
 */
public class BukkitLivingEntity extends BukkitEntity implements PlatformLivingEntity {

    /**
     * Creates a new BukkitLivingEntity wrapper.
     *
     * @param source the source Bukkit living entity
     * @since 2.0.0
     */
    public BukkitLivingEntity(@NotNull LivingEntity source) {
        super(source);
    }

    /**
     * Returns the underlying Bukkit living entity.
     *
     * @return the source living entity
     * @since 2.0.0
     */
    @Override
    public LivingEntity source() {
        return (LivingEntity) super.source();
    }

    @Override
    public @NotNull PlatformLocation eyeLocation() {
        return BukkitAdapter.adapt(source().getEyeLocation());
    }
}

