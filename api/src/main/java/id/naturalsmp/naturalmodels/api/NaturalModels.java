/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api;

import id.naturalsmp.naturalmodels.api.data.renderer.ModelRenderer;
import id.naturalsmp.naturalmodels.api.entity.BaseEntity;
import id.naturalsmp.naturalmodels.api.nms.NMS;
import id.naturalsmp.naturalmodels.api.nms.PlayerChannelHandler;
import id.naturalsmp.naturalmodels.api.platform.PlatformEntity;
import id.naturalsmp.naturalmodels.api.tracker.EntityTrackerRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

/**
 * The main entry point for the NaturalModels API.
 * <p>
 * This class provides static access to the platform instance, configuration, model managers,
 * NMS handlers, and entity registries. It serves as a service provider for interacting with the NaturalModels engine.
 * </p>
 *
 * @since 1.15.2
 */
public final class NaturalModels {

    /**
     * Private initializer to prevent instantiation.
     */
    private NaturalModels() {
        throw new RuntimeException();
    }

    /**
     * The singleton platform instance.
     */
    private static NaturalModelsPlatform instance;

    /**
     * Returns the platform configuration manager.
     *
     * @return the configuration manager
     * @since 1.15.2
     */
    public static @NotNull NaturalModelsConfig config() {
        return platform().config();
    }

    /**
     * Retrieves a model renderer by its name, wrapped in an Optional.
     *
     * @param name the name of the model
     * @return an optional containing the renderer if found
     * @since 1.15.2
     */
    public static @NotNull Optional<ModelRenderer> model(@NotNull String name) {
        return Optional.ofNullable(modelOrNull(name));
    }

    /**
     * Retrieves a model renderer by its name, or null if not found.
     *
     * @param name the name of the model
     * @return the renderer, or null
     * @since 1.15.2
     */
    public static @Nullable ModelRenderer modelOrNull(@NotNull String name) {
        return platform().modelManager().model(name);
    }

    /**
     * Retrieves a player limb renderer by its name, wrapped in an Optional.
     *
     * @param name the name of the limb model
     * @return an optional containing the renderer if found
     * @since 1.15.2
     */
    public static @NotNull Optional<ModelRenderer> limb(@NotNull String name) {
        return Optional.ofNullable(limbOrNull(name));
    }

    /**
     * Retrieves a player limb renderer by its name, or null if not found.
     *
     * @param name the name of the limb model
     * @return the renderer, or null
     * @since 1.15.2
     */
    public static @Nullable ModelRenderer limbOrNull(@NotNull String name) {
        return platform().modelManager().limb(name);
    }

    /**
     * Retrieves a player channel handler by the player's UUID.
     *
     * @param uuid the player's UUID
     * @return an optional containing the channel handler if found
     * @since 1.15.2
     */
    public static @NotNull Optional<PlayerChannelHandler> player(@NotNull UUID uuid) {
        return Optional.ofNullable(platform().playerManager().player(uuid));
    }

    /**
     * Retrieves an entity tracker registry by the entity's UUID.
     *
     * @param uuid the entity's UUID
     * @return an optional containing the registry if found
     * @since 1.15.2
     */
    public static @NotNull Optional<EntityTrackerRegistry> registry(@NotNull UUID uuid) {
        return Optional.ofNullable(registryOrNull(uuid));
    }

    /**
     * Retrieves an entity tracker registry for a Bukkit entity.
     *
     * @param entity the Bukkit entity
     * @return an optional containing the registry if found
     * @since 1.15.2
     */
    public static @NotNull Optional<EntityTrackerRegistry> registry(@NotNull PlatformEntity entity) {
        return Optional.ofNullable(registryOrNull(entity));
    }

    /**
     * Retrieves an entity tracker registry for a base entity.
     *
     * @param entity the base entity
     * @return an optional containing the registry if found
     * @since 1.15.2
     */
    public static @NotNull Optional<EntityTrackerRegistry> registry(@NotNull BaseEntity entity) {
        return Optional.ofNullable(registryOrNull(entity));
    }

    /**
     * Retrieves an entity tracker registry by the entity's UUID, or null if not found.
     *
     * @param uuid the entity's UUID
     * @return the registry, or null
     * @since 1.15.2
     */
    public static @Nullable EntityTrackerRegistry registryOrNull(@NotNull UUID uuid) {
        return EntityTrackerRegistry.registry(uuid);
    }

    /**
     * Retrieves an entity tracker registry for a Bukkit entity, or null if not found.
     *
     * @param entity the Bukkit entity
     * @return the registry, or null
     * @since 1.15.2
     */
    public static @Nullable EntityTrackerRegistry registryOrNull(@NotNull PlatformEntity entity) {
        return registryOrNull(nms().adapt(entity));
    }

    /**
     * Retrieves an entity tracker registry for a base entity, or null if not found.
     *
     * @param entity the base entity
     * @return the registry, or null
     * @since 1.15.2
     */
    public static @Nullable EntityTrackerRegistry registryOrNull(@NotNull BaseEntity entity) {
        return EntityTrackerRegistry.registry(entity);
    }

    /**
     * Returns a collection of all loaded model renderers.
     *
     * @return an unmodifiable collection of models
     * @since 1.15.2
     */
    public static @NotNull @Unmodifiable Collection<ModelRenderer> models() {
        return platform().modelManager().models();
    }

    /**
     * Returns a collection of all loaded player limb renderers.
     *
     * @return an unmodifiable collection of limb models
     * @since 1.15.2
     */
    public static @NotNull @Unmodifiable Collection<ModelRenderer> limbs() {
        return platform().modelManager().limbs();
    }

    /**
     * Returns a set of all loaded model names.
     *
     * @return an unmodifiable set of model keys
     * @since 1.15.2
     */
    public static @NotNull @Unmodifiable Set<String> modelKeys() {
        return platform().modelManager().modelKeys();
    }

    /**
     * Returns a set of all loaded player limb model names.
     *
     * @return an unmodifiable set of limb keys
     * @since 1.15.2
     */
    public static @NotNull @Unmodifiable Set<String> limbKeys() {
        return platform().modelManager().limbKeys();
    }

    /**
     * Returns the singleton instance of the NaturalModels platform.
     *
     * @return the platform instance
     * @throws NullPointerException if the platform has not been initialized
     * @since 2.0.0
     */
    public static @NotNull NaturalModelsPlatform platform() {
        return Objects.requireNonNull(instance, "NaturalModels hasn't been initialized yet!");
    }

    /**
     * Returns the NMS handler instance.
     *
     * @return the NMS handler
     * @since 1.15.2
     */
    public static @NotNull NMS nms() {
        return platform().nms();
    }

    /**
     * Returns the event bus.
     *
     * @return the event bus
     * @since 2.0.0
     */
    public static @NotNull NaturalModelsEventBus eventBus() {
        return platform().eventBus();
    }

    /**
     * Registers the platform instance.
     * <p>
     * This method is intended for internal use only during platform initialization.
     * </p>
     *
     * @param instance the platform instance
     * @throws RuntimeException if an instance is already registered
     * @since 1.15.2
     */
    @ApiStatus.Internal
    public static void register(@NotNull NaturalModelsPlatform instance) {
        Objects.requireNonNull(instance, "instance cannot be null.");
        if (NaturalModels.instance == instance) throw new RuntimeException("Duplicated instance.");
        NaturalModels.instance = instance;
    }
}

