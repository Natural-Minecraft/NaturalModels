/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.bukkit.scheduler;

import id.naturalsmp.naturalmodels.api.scheduler.ModelScheduler;
import id.naturalsmp.naturalmodels.api.scheduler.ModelTask;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Bukkit-specific scheduler for model tasks.
 * <p>
 * This interface extends {@link ModelScheduler} to provide methods for scheduling tasks
 * that are synchronized with specific locations (e.g., for Folia compatibility).
 * </p>
 *
 * @since 2.0.0
 */
public interface BukkitModelScheduler extends ModelScheduler {

    /**
     * Schedules a task to run on the next tick, synchronized with the given location.
     *
     * @param location the location to synchronize with
     * @param runnable the task to run
     * @return the scheduled task, or null if scheduling failed
     * @since 2.0.0
     */
    @Nullable ModelTask task(@NotNull Location location, @NotNull Runnable runnable);

    /**
     * Schedules a task to run after a delay, synchronized with the given location.
     *
     * @param location the location to synchronize with
     * @param delay the delay in ticks
     * @param runnable the task to run
     * @return the scheduled task, or null if scheduling failed
     * @since 2.0.0
     */
    @Nullable ModelTask taskLater(@NotNull Location location, long delay, @NotNull Runnable runnable);
}

