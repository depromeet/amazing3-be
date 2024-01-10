package io.raemian.api.auth.controller.response

import io.raemian.api.auth.domain.UserDTO
import io.raemian.api.goal.controller.response.GoalsResponse
import java.time.LocalDate
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String?,
    val nickname: String?,
    val birth: LocalDate?,
    val image: String,
    val goalCount: Int,
    val createdAt: LocalDateTime?,
) {
    companion object {
        fun of(user: UserDTO, goal: GoalsResponse): UserResponse {
            return UserResponse(
                id = user.id,
                email = user.email,
                username = user.username,
                nickname = user.nickname,
                birth = user.birth,
                image = user.image,
                goalCount = goal.goalsCount,
                createdAt = user.createdAt,
            )
        }
    }
}
