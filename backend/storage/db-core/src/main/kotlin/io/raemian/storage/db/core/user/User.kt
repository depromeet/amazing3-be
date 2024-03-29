package io.raemian.storage.db.core.user

import io.raemian.storage.db.core.common.BaseEntity
import io.raemian.storage.db.core.user.enums.OAuthProvider
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.Nationalized
import java.time.LocalDate

@Entity(name = "USERS")
class User(
    @Column(nullable = false)
    val email: String,

    @Column(unique = true)
    @Nationalized
    val username: String? = null,

    @Column
    @Nationalized
    val nickname: String? = null,

    @Column
    val birth: LocalDate? = null,

    @Column
    val image: String,

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
    fun updateNicknameAndBirth(nickname: String, birth: LocalDate?): User {
        return User(
            email = email,
            nickname = nickname,
            birth = birth,
            username = username,
            image = image,
            provider = provider,
            authority = authority,
            id = id,
        )
    }

    fun updateUsername(username: String): User {
        return User(
            email = email,
            nickname = nickname,
            username = username,
            birth = birth,
            image = image,
            provider = provider,
            authority = authority,
            id = id,
        )
    }

    fun updateNickname(nickname: String): User {
        return User(
            email = email,
            nickname = nickname,
            username = username,
            birth = birth,
            image = image,
            provider = provider,
            authority = authority,
            id = id,
        )
    }

    fun updateImage(image: String): User {
        return User(
            email = email,
            nickname = nickname,
            username = username,
            birth = birth,
            image = image,
            provider = provider,
            authority = authority,
            id = id,
        )
    }
}

enum class Authority {
    ROLE_USER, ROLE_ADMIN
}
