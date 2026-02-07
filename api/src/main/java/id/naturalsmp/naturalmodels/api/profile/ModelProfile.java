/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.profile;

import id.naturalsmp.naturalmodels.api.NaturalModels;
import id.naturalsmp.naturalmodels.api.platform.PlatformOfflinePlayer;
import id.naturalsmp.naturalmodels.api.platform.PlatformPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Model skin
 */
public interface ModelProfile {

    /**
     * Unknown skin
     */
    ModelProfile UNKNOWN = of(ModelProfileInfo.UNKNOWN);

    /**
     * Creates profile
     * @param info info
     * @return profile
     */
    static @NotNull ModelProfile of(@NotNull ModelProfileInfo info) {
        return new Simple(info, ModelProfileSkin.EMPTY);
    }

    /**
     * Creates profile
     * @param info info
     * @param skin skin
     * @return profile
     */
    static @NotNull ModelProfile of(@NotNull ModelProfileInfo info, @NotNull ModelProfileSkin skin) {
        return new Simple(info, skin);
    }

    /**
     * Gets skin by player
     * @param player player
     * @return model profile
     */
    static @NotNull ModelProfile of(@NotNull PlatformPlayer player) {
        var channel = NaturalModels.platform().playerManager().player(player.uuid());
        return channel != null ? channel.base().profile() : NaturalModels.nms().profile(player);
    }

    /**
     * Gets uncompleted profile by offline player
     * @param offlinePlayer offline player
     * @return uncompleted profile
     */
    static @NotNull Uncompleted of(@NotNull PlatformOfflinePlayer offlinePlayer) {
        return NaturalModels.platform().profileManager().supplier().supply(offlinePlayer);
    }

    /**
     * Gets uncompleted profile by offline player's uuid
     * @param uuid offline player's uuid
     * @return uncompleted profile
     */
    static @NotNull Uncompleted of(@NotNull UUID uuid) {
        return of(NaturalModels.platform().adapter().offlinePlayer(uuid));
    }

    /**
     * Gets info
     * @return info
     */
    @NotNull ModelProfileInfo info();

    /**
     * Gets skin
     * @return skin
     */
    @NotNull ModelProfileSkin skin();


    /**
     * Makes this profile as uncompleted
     * @return uncompleted profile
     */
    default @NotNull Uncompleted asUncompleted() {
        return new Uncompleted() {
            @Override
            public @NotNull ModelProfileInfo info() {
                return ModelProfile.this.info();
            }

            @Override
            public @NotNull CompletableFuture<ModelProfile> complete() {
                return CompletableFuture.completedFuture(ModelProfile.this);
            }
        };
    }

    /**
     * Gets player
     * @return player
     */
    default @Nullable PlatformPlayer player() {
        return NaturalModels.platform().adapter().player(info().id());
    }

    /**
     * Simple profile
     * @param info info
     * @param skin skin
     */
    record Simple(@NotNull ModelProfileInfo info, @NotNull ModelProfileSkin skin) implements ModelProfile {
    }

    /**
     * Uncompleted profile
     */
    interface Uncompleted {

        /**
         * Gets info
         * @return info
         */
        @NotNull ModelProfileInfo info();

        /**
         * Completes profile
         * @return completed profile
         */
        @NotNull CompletableFuture<ModelProfile> complete();

        /**
         * Gets fallback profile
         * @return profile
         */
        default @NotNull ModelProfile fallback() {
            return of(info());
        }
    }
}

