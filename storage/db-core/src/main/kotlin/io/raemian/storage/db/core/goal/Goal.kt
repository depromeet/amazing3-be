package io.raemian.storage.db.core.goal

import io.raemian.storage.db.core.BaseEntity
import io.raemian.storage.db.core.emoji.Sticker
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.task.Task
import io.raemian.storage.db.core.user.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.Nationalized
import java.time.LocalDate

@Entity
@Table(name = "GOALS")
class Goal(
    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(nullable = false)
    @Nationalized
    val title: String,

    @Column(nullable = false)
    val deadline: LocalDate,

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "sticker_id")
    val sticker: Sticker,

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "tag_id")
    val tagId: Tag,

    @Column(nullable = false)
    @Nationalized
    val description: String,

    @OneToMany(mappedBy = "goal", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    val tasks: List<Task>,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity()
