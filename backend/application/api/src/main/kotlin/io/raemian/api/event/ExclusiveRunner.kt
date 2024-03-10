package io.raemian.api.event

import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Component
class ExclusiveRunner {
    private val map: ConcurrentHashMap<String, Lock> = ConcurrentHashMap<String, Lock>()

    fun call(key: String, tryLockTimeout: Duration, f: Runnable) {
        val lock = map.computeIfAbsent(key) { key -> ReentrantLock() }

        try {
            if (lock.tryLock(tryLockTimeout.toSeconds(), TimeUnit.SECONDS)) {
                try {
                    f.run()
                    return
                } finally {
                    lock.unlock()
                }
            }
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }

        throw TimeoutException()
    }
}
