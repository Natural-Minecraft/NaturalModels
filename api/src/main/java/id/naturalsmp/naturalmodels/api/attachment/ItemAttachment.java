/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.attachment;

import id.naturalsmp.naturalmodels.api.NaturalModels;
import id.naturalsmp.naturalmodels.api.bone.RenderedBone;
import id.naturalsmp.naturalmodels.api.nms.ModelDisplay;
import id.naturalsmp.naturalmodels.api.nms.PacketBundler;
import id.naturalsmp.naturalmodels.api.platform.PlatformItemStack;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.UUID;

/**
 * An attachment that displays an item at a bone's location.
 *
 * @since 1.15.2
 */
public class ItemAttachment implements BoneAttachment {

    private final ModelDisplay display;

    @Getter
    @Setter
    private Vector3f localOffset = new Vector3f();

    @Getter
    @Setter
    private Vector3f globalOffset = new Vector3f();

    /**
     * Creates a new item attachment.
     *
     * @param itemStack the item to display
     * @since 1.15.2
     */
    public ItemAttachment(@NotNull PlatformItemStack itemStack) {
        this.display = NaturalModels.nms().create(null, d -> {
            d.item(itemStack);
        });
    }

    @Override
    public void update(@NotNull RenderedBone bone, @Nullable UUID player) {
        var pos = bone.worldPosition(localOffset, globalOffset, player);
        var loc = bone.getGroup().getRenderer().location().clone().add(pos.x, pos.y, pos.z);

        // We use a dummy packet bundler for now if we want it synced immediately,
        // but normally this should be called with a bundler?
        // Actually, bone.worldPosition already accounts for interpolation if uuid is
        // provided.

        // For simple teleport, we can just use the display.teleport method.
        // ItemAttachment might need its own bundler or use the tracker's one.
        // Let's assume the caller handles bundling or we perform immediate teleport.

        display.teleport(loc, PacketBundler.EMPTY);
    }

    @Override
    public void remove() {
        display.remove(PacketBundler.EMPTY);
    }

    /**
     * Sets the item to display.
     *
     * @param itemStack the new item stack
     * @since 1.15.2
     */
    public void item(@NotNull PlatformItemStack itemStack) {
        display.item(itemStack);
    }
}
