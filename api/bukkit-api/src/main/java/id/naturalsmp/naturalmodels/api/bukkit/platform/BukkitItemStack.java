/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.bukkit.platform;

import id.naturalsmp.naturalmodels.api.NaturalModels;
import id.naturalsmp.naturalmodels.api.platform.PlatformItemStack;
import id.naturalsmp.naturalmodels.api.platform.PlatformNamespace;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Bukkit item stack wrapped as a {@link PlatformItemStack}.
 *
 * @param source the source Bukkit item stack
 * @since 2.0.0
 */
public record BukkitItemStack(@NotNull ItemStack source) implements PlatformItemStack {
    @Override
    public boolean isAir() {
        return source.getType().isAir() || source.getAmount() <= 0;
    }

    @Override
    public @NotNull PlatformItemStack enchant(boolean enchant) {
        var meta = source.getItemMeta();
        if (meta == null) return this;
        meta.setEnchantmentGlintOverride(enchant);
        source.setItemMeta(meta);
        return this;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull PlatformItemStack modelData(int customModelData, @Nullable PlatformNamespace namespace) {
        var meta = source.getItemMeta();
        if (meta == null) return this;
        meta.setCustomModelData(customModelData);
        if (NaturalModels.platform().version().useItemModelName()) meta.setItemModel(namespace == null ? null : new NamespacedKey(namespace.namespace(), namespace.path()));
        source.setItemMeta(meta);
        return this;
    }

    @Override
    public @NotNull PlatformItemStack clone() {
        return BukkitAdapter.adapt(source.clone());
    }
}

