/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.fabric.scheduler;

import id.naturalsmp.naturalmodels.api.scheduler.ModelScheduler;
import id.naturalsmp.naturalmodels.api.scheduler.ModelTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Fabric-specific scheduler for model tasks.
 * <p>
 * This interface extends {@link ModelScheduler} to provide methods for scheduling tasks
 * within the Fabric environment.
 * </p>
 *
 * @since 2.0.0
 */
public interface FabricModelScheduler extends ModelScheduler {

    /**
     * Schedules a task to run on the next tick.
     *
     * @param runnable the task to run
     * @return the scheduled task, or null if scheduling failed
     * @since 2.0.0
     */
    @Nullable ModelTask task(@NotNull Runnable runnable);

    /**
     * Schedules a task to run after a delay.
     *
     * @param delay the delay in ticks
     * @param runnable the task to run
     * @return the scheduled task, or null if scheduling failed
     * @since 2.0.0
     */
    @Nullable ModelTask taskLater(long delay, @NotNull Runnable runnable);
}

