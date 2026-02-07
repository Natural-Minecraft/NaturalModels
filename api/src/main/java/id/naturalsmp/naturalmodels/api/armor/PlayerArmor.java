/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.armor;

import org.jetbrains.annotations.Nullable;

/**
 * Player armor
 */
public interface PlayerArmor {

    /**
     * Empty armor
     */
    PlayerArmor EMPTY = new PlayerArmor() {
        @Override
        public @Nullable ArmorItem helmet() {
            return null;
        }

        @Override
        public @Nullable ArmorItem chestplate() {
            return null;
        }

        @Override
        public @Nullable ArmorItem leggings() {
            return null;
        }

        @Override
        public @Nullable ArmorItem boots() {
            return null;
        }
    };

    /**
     * Gets helmet
     * @return helmet
     */
    @Nullable ArmorItem helmet();

    /**
     * Gets chestplate
     * @return chestplate
     */
    @Nullable ArmorItem chestplate();

    /**
     * Gets leggings
     * @return leggings
     */
    @Nullable ArmorItem leggings();

    /**
     * Gets boots
     * @return boots
     */
    @Nullable ArmorItem boots();
}

