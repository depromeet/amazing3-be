package io.raemian.api.auth.domain

import io.raemian.storage.db.core.user.User
import java.time.LocalDate
import java.time.LocalDateTime

data class UserDTO(
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
        fun of(user: User): UserDTO {
            return UserDTO(
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
