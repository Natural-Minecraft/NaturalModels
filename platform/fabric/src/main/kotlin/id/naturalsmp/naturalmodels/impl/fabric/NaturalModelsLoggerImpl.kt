/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 NaturalModels
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.impl.fabric

import id.naturalsmp.naturalmodels.api.NaturalModelsLogger
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import java.util.logging.Logger

class NaturalModelsLoggerImpl : NaturalModelsLogger {
    private var logger: ComponentLogger? = null

    private fun logger(): ComponentLogger {
        logger?.let { logger ->
            return logger
        }

        synchronized(this) {
            logger?.let { logger ->
                return logger
            }

            return ComponentLogger.logger(LOGGER.name).also { logger = it }
        }
    }

    override fun info(vararg messages: Component) {
        val logger = logger()
        synchronized(this) {
            for (message in messages) {
                logger.info(message)
            }
        }
    }

    override fun warn(vararg messages: Component) {
        val logger = logger()
        synchronized(this) {
            for (message in messages) {
                logger.warn(message)
            }
        }
    }

    companion object {
        private val LOGGER: Logger = Logger.getLogger(modId())
    }
}


