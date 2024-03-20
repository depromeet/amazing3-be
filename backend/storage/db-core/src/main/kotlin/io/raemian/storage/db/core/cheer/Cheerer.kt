package io.raemian.storage.db.core.cheer

import io.raemian.storage.db.core.common.BaseEntity
import io.raemian.storage.db.core.common.pagination.CursorExtractable
import io.raemian.storage.db.core.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "CHEERER", indexes = [Index(name = "IDX_LIFE_MAP_ID_AND_ID", columnList = "lifeMapId, id")])
class Cheerer(
    @Column
    val lifeMapId: Long,

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = true)
    val user: User?,

    @Column
    val cheeringAt: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity(), CursorExtractable {
    override fun getCursorId(): Long? {
        return id
    }
}
