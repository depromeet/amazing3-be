package io.raemian.api.support.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ProfileHolder(
    @Value("\${spring.profiles.active:local}")
    private val profile: String,
) {
    fun isLive(): Boolean {
        return ProfileType.fromString(profile) == ProfileType.LIVE
    }

    fun isDev(): Boolean {
        return ProfileType.fromString(profile) == ProfileType.DEV
    }
}
