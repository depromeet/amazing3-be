package io.raemian.storage.db.core.cheer

import io.raemian.storage.db.core.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "CHEERING")
class Cheering(
    @Column
    var count: Long,

    @Column
    val lifeMapId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {
    fun addCount(): Cheering {
        this.count += 1
        return this
    }
}
