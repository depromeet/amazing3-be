package io.raemian.api.task.service

import io.raemian.api.support.exception.MaxTaskCountExceededException
import io.raemian.api.task.controller.request.CreateTaskRequest
import io.raemian.api.task.controller.request.RewriteTaskRequest
import io.raemian.api.task.controller.request.UpdateTaskCompletionRequest
import io.raemian.api.task.model.CreateTaskResult
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.task.Task
import io.raemian.storage.db.core.task.TaskJdbcQueryRepository
import io.raemian.storage.db.core.task.TaskRepository
import io.raemian.storage.db.core.task.model.GoalTaskCountQueryResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(
    val taskRepository: TaskRepository,
    val taskJdbcQueryRepository: TaskJdbcQueryRepository,
    val goalRepository: GoalRepository,
) {

    @Transactional
    fun create(currentUserId: Long, createTaskRequest: CreateTaskRequest): CreateTaskResult {
        val goal = goalRepository.getById(createTaskRequest.goalId)
        validateCurrentUserIsGoalOwner(currentUserId, goal)

        val task = Task.createTask(goal, createTaskRequest.description)
        addNewTask(goal, task)

        taskRepository.save(task)
        return CreateTaskResult(task.id!!, task.description)
    }

    @Transactional
    fun rewrite(currentUserId: Long, taskId: Long, rewriteTaskRequest: RewriteTaskRequest) {
        val task = taskRepository.getById(taskId)
        validateCurrentUserIsGoalOwner(currentUserId, task.goal)

        task.rewrite(rewriteTaskRequest.newDescription)
        taskRepository.save(task)
    }

    @Transactional
    fun updateTaskCompletion(
        currentUserId: Long,
        taskId: Long,
        updateTaskCompletionRequest: UpdateTaskCompletionRequest,
    ) {
        val task = taskRepository.getById(taskId)
        validateCurrentUserIsGoalOwner(currentUserId, task.goal)

        task.updateTaskCompletion(updateTaskCompletionRequest.isDone)
        taskRepository.save(task)
    }

    @Transactional
    fun delete(currentUserId: Long, taskId: Long) {
        val task = taskRepository.getById(taskId)
        validateCurrentUserIsGoalOwner(currentUserId, task.goal)

        taskRepository.delete(task)
    }

    @Transactional(readOnly = true)
    fun findGoalTaskCounts(goalIds: List<Long>): List<GoalTaskCountQueryResult> =
        taskJdbcQueryRepository.findAllGoalTaskCountByGoalIdIn(goalIds)

    private fun validateCurrentUserIsGoalOwner(currentUserId: Long, goal: Goal) {
        if (currentUserId != goal.lifeMap.user.id) {
            throw SecurityException()
        }
    }

    private fun addNewTask(goal: Goal, task: Task) {
        try {
            goal.addTask(task)
        } catch (exception: IllegalArgumentException) {
            throw MaxTaskCountExceededException()
        }
    }
}
