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
import org.hibernate.annotations.Nationalized
import java.time.LocalDate

@Entity(name = "USERS")
class User(
    @Column(unique = true, nullable = false)
    val providerId: String,

    @Column(unique = true, nullable = false)
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
    fun updateInfo(nickname: String, birth: LocalDate): User {
        return User(
            providerId = providerId,
            email = email,
            nickname = nickname,
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
