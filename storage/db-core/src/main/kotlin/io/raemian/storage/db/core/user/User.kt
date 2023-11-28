package io.raemian.storage.db.core.user

import io.raemian.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDate

@Entity(name = "USERS")
class User(
    @Column
    val email: String,
    @Column
    val password: String,
    @Column
    val nickname: String? = null,
    @Column
    val birth: LocalDate? = null,
    @Enumerated(EnumType.STRING)
    val authority: Authority,
) : BaseEntity()

enum class Authority {
    ROLE_USER, ROLE_ADMIN
}
