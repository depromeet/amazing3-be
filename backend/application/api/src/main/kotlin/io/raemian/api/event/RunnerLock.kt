package io.raemian.api.event

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

class RunnerLock : ReentrantLock() {
    private val watingThread = AtomicInteger(0)

    fun increase(): Int {
        return watingThread.addAndGet(1)
    }

    fun decrease(): Int {
        return watingThread.decrementAndGet()
    }
}
