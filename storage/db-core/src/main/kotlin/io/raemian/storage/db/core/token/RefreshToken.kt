package io.raemian.storage.db.core.token

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class RefreshToken(
    @Id
    val id: Long,
    val key: String,
    val value: String,
)