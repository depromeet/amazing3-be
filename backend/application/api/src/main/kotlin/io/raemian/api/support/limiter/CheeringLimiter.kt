package io.raemian.api.support.limiter

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class CheeringLimiter {
    private val keyForm = "%s|%s"
    private val timedHashMap = Caffeine.newBuilder()
        .expireAfterWrite(60L, TimeUnit.SECONDS)
        .build<String, Int>()

    fun isPermit(lifeMapId: Long, cheererId: Long): Boolean {
        val key = String.format(keyForm, lifeMapId, cheererId)

        val pastLog = timedHashMap.getIfPresent(key)

        if (pastLog == null) {
            return true
        }

        return false
    }

    fun put(lifeMapId: Long, cheererId: Long) {
        val key = String.format(keyForm, lifeMapId, cheererId)
        timedHashMap.put(key, 1)
    }
}
