package io.raemian.api.user.controller.response

import io.raemian.api.auth.domain.UserDTO
import io.raemian.api.lifemap.domain.LifeMapResponse
import java.time.LocalDate
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String?,
    val nickname: String?,
    val birth: LocalDate?,
    val image: String,
    val createdAt: LocalDateTime?,
    val lifeMap: LifeMapResponse,
) {
    companion object {
        fun of(user: UserDTO, lifeMap: LifeMapResponse): UserResponse {
            return UserResponse(
                id = user.id,
                email = user.email,
                username = user.username,
                nickname = user.nickname,
                birth = user.birth,
                image = user.image,
                createdAt = user.createdAt,
                lifeMap = lifeMap,
            )
        }
    }
}
