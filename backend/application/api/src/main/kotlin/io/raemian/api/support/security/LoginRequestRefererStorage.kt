package io.raemian.api.support.security

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class LoginRequestRefererStorage {
    private val timedStorage = Caffeine.newBuilder()
        .expireAfterWrite(60L, TimeUnit.SECONDS)
        .build<String, String>()

    fun put(state: String, referer: String?) {
        if (referer.isNullOrBlank()) {
            return
        }

        timedStorage.put(state, referer)
    }

    fun get(state: String): String {
        return timedStorage.getIfPresent(state) ?: ""
    }
}
