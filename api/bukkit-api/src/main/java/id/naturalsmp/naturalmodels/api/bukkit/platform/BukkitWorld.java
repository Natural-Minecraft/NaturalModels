/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.bukkit.platform;

import id.naturalsmp.naturalmodels.api.platform.PlatformWorld;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Bukkit world wrapped as a {@link PlatformWorld}.
 *
 * @param source the source Bukkit world
 * @since 2.0.0
 */
public record BukkitWorld(@NotNull World source) implements PlatformWorld {
}

