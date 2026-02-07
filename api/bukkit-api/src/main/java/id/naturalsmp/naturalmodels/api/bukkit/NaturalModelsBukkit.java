/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.bukkit;

import id.naturalsmp.naturalmodels.api.NaturalModels;
import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform;
import id.naturalsmp.naturalmodels.api.bukkit.platform.BukkitAdapter;
import id.naturalsmp.naturalmodels.api.bukkit.scheduler.BukkitModelScheduler;
import org.jetbrains.annotations.NotNull;

import static id.naturalsmp.naturalmodels.api.util.ReflectionUtil.classExists;

/**
 * Represents the Bukkit-specific platform interface for NaturalModels.
 * <p>
 * This interface extends {@link NaturalModelsPlatform} to provide Bukkit-specific implementations
 * for scheduling and entity adaptation.
 * </p>
 *
 * @since 2.0.0
 */
public interface NaturalModelsBukkit extends NaturalModelsPlatform {

    /**
     * Checks if the server is running on the Folia platform.
     * @since 2.0.0
     */
    boolean IS_FOLIA = classExists("io.papermc.paper.threadedregions.RegionizedServer");
    /**
     * Checks if the server is running on the Purpur platform.
     * @since 2.0.0
     */
    boolean IS_PURPUR = classExists("org.purpurmc.purpur.PurpurConfig");
    /**
     * Checks if the server is running on the Paper platform (or a fork like Purpur/Folia).
     * @since 2.0.0
     */
    boolean IS_PAPER = IS_PURPUR || IS_FOLIA || classExists("io.papermc.paper.configuration.PaperConfigurations");

    /**
     * Returns the current {@link NaturalModelsBukkit} instance.
     *
     * @return the current platform instance
     * @since 2.0.0
     */
    static @NotNull NaturalModelsBukkit platform() {
        return (NaturalModelsBukkit) NaturalModels.platform();
    }

    /**
     * Returns the Bukkit-specific scheduler.
     *
     * @return the scheduler
     * @since 2.0.0
     */
    @Override
    @NotNull BukkitModelScheduler scheduler();

    /**
     * Returns the Bukkit-specific adapter.
     *
     * @return the adapter
     * @since 2.0.0
     */
    @Override
    @NotNull BukkitAdapter adapter();

    /**
     * Returns the Bukkit-specific event bus.
     *
     * @return the event bus
     * @since 2.0.0
     */
    @Override
    @NotNull BukkitModelEventBus eventBus();
}

