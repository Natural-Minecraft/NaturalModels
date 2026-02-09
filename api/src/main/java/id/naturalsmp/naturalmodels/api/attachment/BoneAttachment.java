/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.attachment;

import id.naturalsmp.naturalmodels.api.bone.RenderedBone;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents an attachment that is linked to a specific bone.
 * <p>
 * Attachments are updated every frame to match the bone's world position and
 * rotation.
 * </p>
 *
 * @since 1.15.2
 */
public interface BoneAttachment {

    /**
     * Updates the attachment's position and state based on the bone's
     * transformation.
     *
     * @param bone   the bone this attachment is linked to
     * @param player the player viewing the model (can be null for global update)
     * @since 1.15.2
     */
    void update(@NotNull RenderedBone bone, @Nullable UUID player);

    /**
     * Removes and cleans up the attachment.
     *
     * @since 1.15.2
     */
    void remove();
}
