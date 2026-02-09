/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.test;

import id.naturalsmp.naturalmodels.api.NaturalModels;
import id.naturalsmp.naturalmodels.api.attachment.ItemAttachment;
import id.naturalsmp.naturalmodels.api.bukkit.platform.BukkitAdapter;
import id.naturalsmp.naturalmodels.api.platform.PlatformItemStack;
import id.naturalsmp.naturalmodels.api.tracker.TrackerModifier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class AttachmentTester implements ModelTester {

    @Override
    public void start(@NotNull NaturalModelsTest test) {
        var command = test.getCommand("attachment_test");
        if (command != null) {
            command.setExecutor((sender, cmd, label, args) -> {
                if (!(sender instanceof Player player))
                    return false;

                var item = new ItemStack(Material.DIAMOND);
                var platformItem = PlatformItemStack.of(item);

                NaturalModels.limb("steve")
                        .map(r -> r.getOrCreate(BukkitAdapter.adapt(player), TrackerModifier.DEFAULT, t -> {
                        }))
                        .ifPresent(tracker -> {
                            tracker.pipeline().bone("head").ifPresent(bone -> {
                                var attachment = new ItemAttachment(platformItem);
                                bone.attach(attachment);
                                player.sendMessage("Attached Diamond to head!");
                            });
                        });

                return true;
            });
        }
    }

    @Override
    public void end(@NotNull NaturalModelsTest test) {
    }
}
