package io.raemian.storage.db.core.profile

import io.raemian.storage.db.core.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Nationalized

@Entity
@Table(name = "DEFAULT_PROFILES")
class DefaultProfile(
    @Column(nullable = false)
    @Nationalized
    var name: String,

    @Column(nullable = false)
    var url: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {
    fun updateNameAndUrl(
        name: String,
        url: String,
    ) {
        this.name = name
        this.url = url
    }
}
