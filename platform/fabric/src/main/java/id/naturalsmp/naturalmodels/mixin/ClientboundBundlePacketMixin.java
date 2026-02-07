/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.mixin;

import id.naturalsmp.naturalmodels.impl.fabric.network.NaturalModelsBundlePacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ClientboundBundlePacket.class)
public abstract class ClientboundBundlePacketMixin implements NaturalModelsBundlePacket {
    @Unique
    private boolean NaturalModels$isNaturalModelsPacket;

    @Override
    public boolean NaturalModels$isNaturalModelsPacket() {
        return NaturalModels$isNaturalModelsPacket;
    }

    @Override
    public void NaturalModels$setNaturalModelsPacket(boolean isNaturalModels) {
        NaturalModels$isNaturalModelsPacket = isNaturalModels;
    }
}

