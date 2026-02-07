/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Display;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Display.class)
public interface DisplayAccessor {
    @Accessor("DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID")
    static @NotNull EntityDataAccessor<Integer> NaturalModels$getDataTransformationInterpolationStartDeltaTicksId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID")
    static @NotNull EntityDataAccessor<Integer> NaturalModels$getDataTransformationInterpolationDurationId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_POS_ROT_INTERPOLATION_DURATION_ID")
    static @NotNull EntityDataAccessor<Integer> NaturalModels$getDataPosRotInterpolationDurationId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_TRANSLATION_ID")
    static @NotNull EntityDataAccessor<Vector3fc> NaturalModels$getDataTranslationId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_SCALE_ID")
    static @NotNull EntityDataAccessor<Vector3fc> NaturalModels$getDataScaleId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_LEFT_ROTATION_ID")
    static @NotNull EntityDataAccessor<Quaternionfc> NaturalModels$getDataLeftRotationId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_RIGHT_ROTATION_ID")
    static @NotNull EntityDataAccessor<Quaternionfc> NaturalModels$getDataRightRotationId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_BILLBOARD_RENDER_CONSTRAINTS_ID")
    static @NotNull EntityDataAccessor<Byte> NaturalModels$getDataBillboardRenderConstraintsId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_BRIGHTNESS_OVERRIDE_ID")
    static @NotNull EntityDataAccessor<Integer> NaturalModels$getDataBrightnessOverrideId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_VIEW_RANGE_ID")
    static @NotNull EntityDataAccessor<Float> NaturalModels$getDataViewRangeId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_SHADOW_RADIUS_ID")
    static @NotNull EntityDataAccessor<Float> NaturalModels$getDataShadowRadiusId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_SHADOW_STRENGTH_ID")
    static @NotNull EntityDataAccessor<Float> NaturalModels$getDataShadowStrengthId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_WIDTH_ID")
    static @NotNull EntityDataAccessor<Float> NaturalModels$getDataWidthId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_HEIGHT_ID")
    static @NotNull EntityDataAccessor<Float> NaturalModels$getDataHeightId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Accessor("DATA_GLOW_COLOR_OVERRIDE_ID")
    static @NotNull EntityDataAccessor<Integer> NaturalModels$getDataGlowColorOverrideId() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }
}

