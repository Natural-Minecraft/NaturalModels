/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.bukkit;

import id.naturalsmp.naturalmodels.api.NaturalModelsEventBus;
import id.naturalsmp.naturalmodels.api.bukkit.event.BukkitEventApplication;
import id.naturalsmp.naturalmodels.api.event.ModelEvent;
import id.naturalsmp.naturalmodels.api.event.ModelEventListener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A Bukkit-specific extension of the {@link NaturalModelsEventBus}.
 * <p>
 * This interface provides convenience methods for subscribing to events using a Bukkit {@link Plugin} instance.
 * </p>
 *
 * @since 2.0.0
 */
public interface BukkitModelEventBus extends NaturalModelsEventBus {

    /**
     * Subscribes a consumer to a specific event type, associated with a Bukkit plugin.
     *
     * @param plugin the plugin that subscribes to the event
     * @param eventClass the class of the event to subscribe to
     * @param consumer the consumer to handle the event
     * @param <T> the type of the event
     * @return a listener handle that can be used to unregister the subscription
     * @since 2.0.0
     */
    @NotNull
    default <T extends ModelEvent> ModelEventListener subscribe(@NotNull Plugin plugin, @NotNull Class<T> eventClass, @NotNull Consumer<T> consumer) {
        return subscribe(BukkitEventApplication.of(plugin), eventClass, consumer);
    }
}

