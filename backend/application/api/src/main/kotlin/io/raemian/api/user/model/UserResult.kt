package io.raemian.api.user.model

import io.raemian.storage.db.core.user.User
import java.time.LocalDate
import java.time.LocalDateTime

data class UserResult(
    val id: Long,
    val email: String,
    val username: String? = null,
    val nickname: String? = null,
    val birth: LocalDate? = null,
    val image: String,
    val provider: String,
    val authority: String,
    val createdAt: LocalDateTime? = null,
) {
    companion object {
        fun of(user: User): UserResult {
            return UserResult(
                id = user.id!!,
                email = user.email,
                nickname = user.nickname,
                username = user.username,
                birth = user.birth,
                image = user.image,
                provider = user.provider.name,
                authority = user.authority.name,
                createdAt = user.createdAt,
            )
        }
    }
}
