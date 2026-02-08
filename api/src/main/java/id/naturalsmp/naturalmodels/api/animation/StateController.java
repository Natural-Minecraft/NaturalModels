package id.naturalsmp.naturalmodels.api.animation;

import id.naturalsmp.naturalmodels.api.entity.BaseEntity;
import id.naturalsmp.naturalmodels.api.tracker.Tracker;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages animation states for a tracker.
 */
@RequiredArgsConstructor
public class StateController {

    private final @NotNull Tracker tracker;
    private final List<AnimationState> states = new CopyOnWriteArrayList<>();
    private @Nullable AnimationState currentState;

    /**
     * Adds an animation state.
     * 
     * @param state the state
     */
    public void addState(@NotNull AnimationState state) {
        states.add(state);
        states.sort(Comparator.comparingInt(AnimationState::getPriority).reversed());
    }

    /**
     * Removes an animation state by name.
     * 
     * @param name the state name
     */
    public void removeState(@NotNull String name) {
        states.removeIf(s -> s.getName().equalsIgnoreCase(name));
    }

    /**
     * Updates the current state based on the entity behavior.
     * 
     * @param entity the entity
     */
    public void update(@NotNull BaseEntity entity) {
        AnimationState bestState = states.stream()
                .filter(s -> s.isActive(entity))
                .findFirst()
                .orElse(null);

        if (!Objects.equals(bestState, currentState)) {
            if (currentState != null) {
                tracker.stopAnimation(currentState.getAnimation());
            }
            currentState = bestState;
            if (currentState != null) {
                tracker.animate(currentState.getAnimation(), currentState.getModifier());
            }
        }
    }
}
