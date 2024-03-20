package io.raemian.api.support.lock

import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Component
class ExclusiveRunner {
    private val map: ConcurrentHashMap<String, RunnerLock> = ConcurrentHashMap<String, RunnerLock>()

    fun call(key: String, tryLockTimeout: Duration, f: Runnable) {
        val lock = map.computeIfAbsent(key) { key -> RunnerLock() }

        try {
            lock.increase()
            if (lock.tryLock(tryLockTimeout.toSeconds(), TimeUnit.SECONDS)) {
                try {
                    f.run()
                    return
                } finally {
                    if (lock.decrease() <= 0) {
                        map.remove(key)
                    }
                    lock.unlock()
                }
            }
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }

        throw TimeoutException()
    }
}
