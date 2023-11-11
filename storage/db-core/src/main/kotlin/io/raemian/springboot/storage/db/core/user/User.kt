package io.raemian.springboot.storage.db.core.member

import io.raemian.springboot.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class Member(
    @Column
    val email: String,
    @Column
    val password: String
) : BaseEntity()