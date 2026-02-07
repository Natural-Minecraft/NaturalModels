/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.impl.fabric

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun modId() = MOD_ID

private const val MOD_ID = "NaturalModels"

fun logger(): Logger = LOGGER

private val LOGGER: Logger = LoggerFactory.getLogger(modId())


