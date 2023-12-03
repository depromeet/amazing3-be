package io.raemian.storage.db.core.user

import io.raemian.storage.db.core.BaseEntity
import io.raemian.storage.db.core.user.enums.OAuthProvider
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate

@Entity(name = "USERS")
class User(
    @Column
    val email: String,

    @Column
    val nickname: String? = null,

    @Column
    val birth: LocalDate? = null,

    @Column
    @Enumerated(EnumType.STRING)
    val provider: OAuthProvider,

    @Column
    @Enumerated(EnumType.STRING)
    val authority: Authority,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {
    fun updateInfo(nickname: String, birth: LocalDate): User {
        return User(
            email = email,
            nickname = nickname,
            birth = birth,
            provider = provider,
            authority = authority,
            id = id,
        )
    }
}

enum class Authority {
    ROLE_USER, ROLE_ADMIN
}
