package io.raemian.springboot.storage.db.core.user

import io.raemian.springboot.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class User(
    @Column
    val email: String,
    @Column
    val password: String
) : BaseEntity()


enum class Authority {
    ROLE_USER, ROLE_ADMIN
}