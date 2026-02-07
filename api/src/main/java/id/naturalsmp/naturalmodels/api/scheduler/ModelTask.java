/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.api.scheduler;

/**
 * A scheduled task of NaturalModels
 */
public interface ModelTask {

    /**
     * Checks this task is canceled
     * @return whether to cancel
     */
    boolean isCancelled();

    /**
     * Cancels this task
     */
    void cancel();
}

