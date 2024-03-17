package io.raemian.admin.lifemap.model

import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.task.Task

data class GoalResult(
    val title: String,
    val description: String,
    val deadline: String,
    val stickerUrl: String,
    val tagInfo: TagInfo,
    val tasks: List<TaskInfo>,
) {

    constructor(goal: Goal) : this(
        goal.title,
        goal.description,
        goal.deadline.toString(),
        goal.sticker.url,
        TagInfo(goal.tag),
        goal.tasks.map(GoalResult::TaskInfo),
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
