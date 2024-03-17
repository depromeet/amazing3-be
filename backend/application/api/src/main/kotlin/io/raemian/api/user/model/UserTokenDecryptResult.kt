package io.raemian.api.user.model

import io.raemian.api.lifemap.model.LifeMapResult
import java.time.LocalDate
import java.time.LocalDateTime

data class UserTokenDecryptResult(
    val id: Long,
    val email: String,
    val username: String?,
    val nickname: String?,
    val birth: LocalDate?,
    val image: String,
    val lifeMap: LifeMapResult,
    val createdAt: LocalDateTime?,
) {
    companion object {
        fun of(user: UserResult, lifeMap: LifeMapResult): UserTokenDecryptResult {
            return UserTokenDecryptResult(
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
