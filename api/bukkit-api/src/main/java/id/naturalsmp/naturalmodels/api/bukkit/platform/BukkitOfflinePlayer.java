/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.bukkit.platform;

import id.naturalsmp.naturalmodels.api.platform.PlatformOfflinePlayer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents a Bukkit offline player wrapped as a {@link PlatformOfflinePlayer}.
 *
 * @param source the source Bukkit offline player
 * @since 2.0.0
 */
public record BukkitOfflinePlayer(@NotNull OfflinePlayer source) implements PlatformOfflinePlayer {
    @Override
    public @NotNull UUID uuid() {
        return source.getUniqueId();
    }

    @Override
    public @Nullable String name() {
        return source.getName();
    }
}

