package id.naturalsmp.naturalmodels.api.animation;

import id.naturalsmp.naturalmodels.api.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Represents an animation state.
 */
@Getter
@Builder
public class AnimationState {
    private final @NotNull String name;
    private final @NotNull String animation;
    private final int priority;
    private final @NotNull Predicate<BaseEntity> predicate;
    private final @NotNull AnimationModifier modifier;

    /**
     * Checks if this state is active for the given entity.
     * 
     * @param entity the entity
     * @return true if active
     */
    public boolean isActive(@NotNull BaseEntity entity) {
        return predicate.test(entity);
    }
}
