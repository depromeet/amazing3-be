package io.raemian.api.support.lock

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

class RunnerLock : ReentrantLock() {
    private val watingThreadCount = AtomicInteger(0)

    fun increase(): Int {
        return watingThreadCount.addAndGet(1)
    }

    fun decrease(): Int {
        return watingThreadCount.decrementAndGet()
    }
}
