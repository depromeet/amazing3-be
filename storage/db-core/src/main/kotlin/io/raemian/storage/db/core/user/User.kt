package io.raemian.storage.db.core.user

import io.raemian.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity(name = "USERS")
class User(
    @Column
    val email: String,
    @Column
    val password: String,

    @Enumerated(EnumType.STRING)
    val authority: Authority,
) : BaseEntity()

enum class Authority {
    ROLE_USER, ROLE_ADMIN
}
