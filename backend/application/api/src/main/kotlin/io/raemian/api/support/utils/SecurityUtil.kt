package io.raemian.api.support.utils

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtil {
    // SecurityContext 에 유저 정보가 저장되는 시점
    fun currentUserId(): Long {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication == null || authentication.name == null) {
            throw RuntimeException("Security Context 에 인증 정보가 없습니다.")
        }
        return authentication.name.toLong()
    }
}
