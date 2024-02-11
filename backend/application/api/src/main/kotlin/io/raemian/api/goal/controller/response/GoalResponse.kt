package io.raemian.api.goal.controller.response

import io.raemian.api.support.format
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.tag.Tag
import io.raemian.storage.db.core.task.Task

data class GoalResponse(
    val title: String,
    val description: String,
    val deadline: String,
    val stickerUrl: String,
    val tagInfo: TagInfo,
    val isMyGoal: Boolean,
    val tasks: List<TaskInfo>,
) {

    constructor(goal: Goal, isMyGoal: Boolean) : this(
        title = goal.title,
        description = goal.description,
        deadline = goal.deadline.format(),
        stickerUrl = goal.sticker.url,
        tagInfo = TagInfo(goal.tag),
        isMyGoal = isMyGoal,
        tasks = goal.tasks.map(::TaskInfo),
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
