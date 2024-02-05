package io.raemian.storage.db.core.goal

import io.raemian.storage.db.core.BaseEntity
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
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.Nationalized
import java.time.LocalDate

@Entity
@Table(name = "GOALS")
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
    ) {
        this.title = title
        this.deadline = deadline
        this.description = description
    }

    fun updateSticker(sticker: Sticker) {
        this.sticker = sticker
    }

    fun updateTag(tag: Tag) {
        this.tag = tag
    }

    private fun validateMaxTaskCount() =
        require(tasks.size < MAX_TASK_COUNT)
}
