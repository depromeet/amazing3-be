package io.raemian.storage.db.core.cheer

import io.raemian.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "\"CHEERER\"", indexes = [Index(name = "IDX_LIFE_MAP_ID_AND_CHEERING_AT", columnList = "lifeMapId, cheeringAt")])
class Cheerer (
    @Column
    val lifeMapId: Long,

    @Column
    val userId: Long,

    @Column
    val username: String,

    @Column
    val userImage: String,

    @Column
    val cheeringAt: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
): BaseEntity()