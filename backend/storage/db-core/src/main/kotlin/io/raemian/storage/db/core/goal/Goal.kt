package io.raemian.storage.db.core.goal

import io.raemian.storage.db.core.common.BaseEntity
import io.raemian.storage.db.core.lifemap.LifeMap
import io.raemian.storage.db.core.sticker.Sticker
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.task.Task
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.Nationalized
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "GOALS",  indexes = [Index(name = "IDX_DEADLINE", columnList = "deadline")])
class Goal(
    @ManyToOne
    @JoinColumn(name = "life_map_id", nullable = false)
    val lifeMap: LifeMap,

    @Column(nullable = false)
    @Nationalized
    var title: String,

    @Column(nullable = false)
    var deadline: LocalDate,

    @ManyToOne
    @JoinColumn(name = "sticker_id", nullable = false)
    var sticker: Sticker,

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    var tag: Tag,

    @Nationalized
    var description: String = "",

    @Column(name = "lastCommentReadAt", nullable = false)
    val lastCommentReadAt: LocalDateTime,

    @OneToMany(
        mappedBy = "goal",
        cascade = [CascadeType.REMOVE, CascadeType.MERGE],
        fetch = FetchType.LAZY,
    )
    val tasks: MutableList<Task> = ArrayList(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {

    companion object {
        private const val MAX_TASK_COUNT = 50
    }

    fun addTask(task: Task) {
        validateMaxTaskCount()
        tasks.add(task)
    }

    fun update(
        title: String,
        deadline: LocalDate,
        description: String,
    ): Goal =
        Goal(lifeMap, title, deadline, sticker, tag, description, lastCommentReadAt, tasks, id)

    fun updateSticker(sticker: Sticker): Goal =
        Goal(lifeMap, title, deadline, sticker, tag, description, lastCommentReadAt, tasks, id)

    fun updateTag(tag: Tag): Goal =
        Goal(lifeMap, title, deadline, sticker, tag, description, lastCommentReadAt, tasks, id)

    private fun validateMaxTaskCount() =
        require(tasks.size < MAX_TASK_COUNT)
}
