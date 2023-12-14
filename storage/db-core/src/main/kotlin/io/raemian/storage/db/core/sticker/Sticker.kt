package io.raemian.storage.db.core.sticker

import io.raemian.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Nationalized

@Entity
@Table(name = "STICKERS")
class Sticker(
    @Column(nullable = false)
    @Nationalized
    val name: String,

    @Embedded
    @Column(nullable = false)
    val stickerImage: StickerImage,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity()
