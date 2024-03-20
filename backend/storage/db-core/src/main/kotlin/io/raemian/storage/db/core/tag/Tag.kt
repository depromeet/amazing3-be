package io.raemian.storage.db.core.tag

import io.raemian.storage.db.core.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Nationalized

@Entity
@Table(name = "TAGS")
class Tag(
    @Column(nullable = false)
    @Nationalized
    var content: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {
    fun updateContent(content: String) {
        this.content = content
    }
}
