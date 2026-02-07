/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.fabric;

import id.naturalsmp.naturalmodels.api.NaturalModels;
import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform;
import id.naturalsmp.naturalmodels.api.fabric.scheduler.FabricModelScheduler;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the Fabric-specific platform interface for NaturalModels.
 * <p>
 * This interface extends {@link NaturalModelsPlatform} to provide access to the underlying
 * Minecraft server instance and region holder for thread-safe operations.
 * </p>
 *
 * @since 2.0.0
 */
public interface NaturalModelsFabric extends NaturalModelsPlatform {

    /**
     * Returns the current {@link NaturalModelsFabric} instance.
     *
     * @return the current platform instance
     * @since 2.0.0
     */
    static @NotNull NaturalModelsFabric platform() {
        return (NaturalModelsFabric) NaturalModels.platform();
    }

    /**
     * Returns the underlying Minecraft server instance.
     *
     * @return the Minecraft server
     * @since 2.0.0
     */
    @NotNull MinecraftServer server();

    /**
     * Returns the Fabric-specific scheduler.
     *
     * @return the scheduler
     * @since 2.0.0
     */
    @Override
    @NotNull FabricModelScheduler scheduler();
}

