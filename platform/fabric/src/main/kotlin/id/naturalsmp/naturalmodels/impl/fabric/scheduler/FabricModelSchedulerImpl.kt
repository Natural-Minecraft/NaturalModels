/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.impl.fabric.scheduler

import id.naturalsmp.naturalmodels.api.fabric.platform.FabricRegionHolder
import id.naturalsmp.naturalmodels.api.fabric.scheduler.FabricModelScheduler
import id.naturalsmp.naturalmodels.api.scheduler.ModelTask
import id.naturalsmp.naturalmodels.api.util.LogUtil
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object FabricModelSchedulerImpl : FabricModelScheduler, FabricRegionHolder {

    private val scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), object : ThreadFactory {

        private val integer = AtomicInteger()

        override fun newThread(r: Runnable): Thread {
            val thread = Thread(r)
            thread.setDaemon(true)
            thread.setName("NaturalModels-Async-Scheduler-" + integer.getAndIncrement())
            thread.setUncaughtExceptionHandler { t: Thread, e: Throwable -> LogUtil.handleException("Exception has occurred in " + t.name, e) }
            return thread
        }
    })

    private val enabled = AtomicBoolean(true)

    private val queue = ConcurrentLinkedQueue<SyncTask>()

    override fun asyncTask(runnable: Runnable): ModelTask {
        return AsyncTask(runnable) {
            submit(it)
        }
    }

    override fun asyncTaskLater(delay: Long, runnable: Runnable): ModelTask {
        return AsyncTask(runnable) {
           schedule(it, delay * 50, TimeUnit.MILLISECONDS)
        }
    }

    override fun asyncTaskTimer(delay: Long, period: Long, runnable: Runnable): ModelTask {
        return AsyncTask(runnable) {
            scheduleAtFixedRate(it, delay * 50, period * 50, TimeUnit.MILLISECONDS)
        }
    }

    override fun task(runnable: Runnable): ModelTask? {
        if (!enabled.get()) return null
        return SyncTask(runnable).apply { queue += this }
    }

    override fun taskLater(delay: Long, runnable: Runnable): ModelTask? {
        if (!enabled.get()) return null
        return SyncTask(runnable, delay).apply { queue += this }
    }

    private class AsyncTask(
        private val runnable: Runnable,
        scheduleFunction: (ScheduledExecutorService).(Runnable) -> Future<*>
    ) : Runnable, ModelTask {

        private val future = scheduler.scheduleFunction(this)

        override fun run() {
            if (enabled.get()) {
                runnable.run()
            } else {
                future.cancel(true)
            }
        }
        override fun isCancelled(): Boolean = future.isCancelled

        override fun cancel() {
            future.cancel(true)
        }
    }

    private class SyncTask(
        @Volatile
        var task: Runnable,
        counter: Long = 0L
    ) : ModelTask {
        private val atomicCounter = AtomicLong(counter)

        fun run() = if (atomicCounter.getAndDecrement() <= 0) {
            synchronized(this) {
                if (enabled.get()) task.run()
            }
            true
        } else false

        override fun isCancelled(): Boolean {
            return task === CANCELLED_TASK
        }

        override fun cancel() {
            if (isCancelled) return
            synchronized(this) {
                if (isCancelled) return
                task = CANCELLED_TASK
                atomicCounter.set(0)
            }
        }

        companion object {
            val CANCELLED_TASK: Runnable = {}
        }
    }

    private fun tick() {
        queue.removeIf {
            it.run()
        }
    }

    fun init() {
        ServerTickEvents.START_WORLD_TICK.register {
            tick()
        }

        ServerLifecycleEvents.SERVER_STARTING.register {
            enabled.set(true)
        }

        ServerLifecycleEvents.SERVER_STOPPED.register {
            enabled.set(false)
        }
    }
}


