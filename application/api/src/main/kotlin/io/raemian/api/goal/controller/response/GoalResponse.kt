package io.raemian.api.goal.controller.response

import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.sticker.StickerImage
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.task.Task
import java.time.LocalDate

data class GoalResponse(
    val title: String,
    val deadline: LocalDate,
    val sticker: StickerImage,
    val tagInfo: TagInfo,
    val tasks: List<TaskInfo>,
) {

    constructor(goal: Goal) : this(
        goal.title,
        goal.deadline,
        goal.sticker.stickerImage,
        TagInfo(goal.tag),
        goal.tasks.map(::TaskInfo),
    )

    data class TagInfo(
        val tagId: Long?,
        val tagContent: String,
    ) {

        constructor(tag: Tag) : this(tag.id, tag.content)
    }

    data class TaskInfo(
        val taskId: Long?,
        val isTaskDone: Boolean,
        val taskDescription: String,
    ) {

        constructor(task: Task) : this(task.id, task.isDone, task.description)
    }
}
