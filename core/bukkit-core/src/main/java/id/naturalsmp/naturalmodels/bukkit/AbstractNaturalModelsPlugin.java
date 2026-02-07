/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit;

import id.naturalsmp.naturalmodels.NaturalModelsPlatformImpl;
import id.naturalsmp.naturalmodels.api.NaturalModels;
import id.naturalsmp.naturalmodels.api.NaturalModelsLogger;
import id.naturalsmp.naturalmodels.api.bukkit.NaturalModelsBukkit;
import id.naturalsmp.naturalmodels.api.bukkit.platform.BukkitAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public abstract class AbstractNaturalModelsPlugin extends JavaPlugin implements NaturalModelsPlatformImpl, NaturalModelsBukkit {

    protected boolean skipInitialReload;
    protected final AtomicBoolean onReload = new AtomicBoolean();
    protected final AtomicBoolean firstLoad = new AtomicBoolean();
    protected final BukkitAdapter adapter = new BukkitAdapter();
    protected final NaturalModelsLogger logger = new NaturalModelsLogger() {

        private ComponentLogger internalLogger;

        private @NotNull ComponentLogger logger() {
            if (internalLogger != null) return internalLogger;
            synchronized (this) {
                if (internalLogger != null) return internalLogger;
                return internalLogger = ComponentLogger.logger(getLogger().getName());
            }
        }

        @Override
        public void info(@NotNull Component... message) {
            var log = logger();
            synchronized (this) {
                for (Component s : message) {
                    log.info(s);
                }
            }
        }

        @Override
        public void warn(@NotNull Component... message) {
            var log = logger();
            synchronized (this) {
                for (Component s : message) {
                    log.warn(s);
                }
            }
        }
    };
    private @Nullable Attributes attributes;

    public void onLoad() {
        new NaturalModelsLibrary().load(this);
        NaturalModels.register(this);
    }

    public void skipInitialReload() {
        this.skipInitialReload = true;
    }

    @Override
    public void saveResource(@NotNull String resourcePath) {
        saveResource(resourcePath, false);
    }

    @Override
    @NotNull
    public BukkitAdapter adapter() {
        return adapter;
    }

    public @NotNull Attributes attributes() {
        if (attributes != null) return attributes;
        synchronized (this) {
            if (attributes != null) return attributes;
            try (
                var stream = Objects.requireNonNull(getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"))
            ) {
                return attributes = new Manifest(stream).getMainAttributes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

