/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.fabric.entity;

import id.naturalsmp.naturalmodels.api.entity.BaseEntity;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Fabric-specific entity adapter.
 * <p>
 * This interface extends {@link BaseEntity} to provide access to the underlying NMS entity.
 * </p>
 *
 * @since 2.0.0
 */
public interface BaseFabricEntity extends BaseEntity {

    /**
     * Returns the underlying NMS entity.
     *
     * @return the NMS entity
     * @since 2.0.0
     */
    default @NotNull Entity entity() {
        return (Entity) handle();
    }

    /**
     * Sets the underlying NMS entity.
     *
     * @param entity the NMS entity
     * @since 2.0.0
     */
    void entity(@NotNull Entity entity);
}

