package io.raemian.admin.user.controller.response

import io.raemian.storage.db.core.user.User
import java.time.LocalDate

data class UserResponse(
    val id: Long?,
    val email: String,
    val username: String?,
    val nickname: String?,
    val birth: LocalDate?,
    val image: String,
    val provider: String,
    val authority: String,
) {

    companion object {
        fun from(entity: User): UserResponse {
            return UserResponse(
                entity.id,
                entity.email,
                entity.username,
                entity.nickname,
                entity.birth,
                entity.image,
                entity.provider.name,
                entity.authority.name,
            )
        }
    }
}
