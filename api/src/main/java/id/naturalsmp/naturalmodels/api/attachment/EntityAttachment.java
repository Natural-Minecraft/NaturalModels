/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.attachment;

import id.naturalsmp.naturalmodels.api.bone.RenderedBone;
import id.naturalsmp.naturalmodels.api.platform.PlatformEntity;
import id.naturalsmp.naturalmodels.api.platform.PlatformLocation;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.UUID;

/**
 * An attachment that syncs a platform entity with a bone's location.
 *
 * @since 1.15.2
 */
public class EntityAttachment implements BoneAttachment {

    private final PlatformEntity entity;

    @Getter
    @Setter
    private Vector3f localOffset = new Vector3f();

    @Getter
    @Setter
    private Vector3f globalOffset = new Vector3f();

    /**
     * Creates a new entity attachment.
     *
     * @param entity the entity to sync
     * @since 1.15.2
     */
    public EntityAttachment(@NotNull PlatformEntity entity) {
        this.entity = entity;
    }

    @Override
    public void update(@NotNull RenderedBone bone, @Nullable UUID player) {
        var pos = bone.worldPosition(localOffset, globalOffset, player);
        var loc = bone.getRenderContext().source().location().add(pos.x, pos.y, pos.z);

        // Use immediate teleport for the entity.
        entity.teleport(loc);
    }

    @Override
    public void remove() {
        // We don't necessarily want to remove the entity, maybe just stop syncing?
        // But for parity with ItemAttachment, maybe we should have an option?
        // For now, let's just leave it up to the user to handle entity removal if
        // needed,
        // or we could add a flag.
    }
}
