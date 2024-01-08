package io.raemian.api.task

import io.raemian.api.task.controller.request.CreateTaskRequest
import io.raemian.api.task.controller.request.RewriteTaskRequest
import io.raemian.api.task.controller.request.UpdateTaskCompletionRequest
import io.raemian.api.task.controller.response.CreateTaskResponse
import io.raemian.storage.db.core.goal.Goal
import io.raemian.storage.db.core.goal.GoalRepository
import io.raemian.storage.db.core.task.Task
import io.raemian.storage.db.core.task.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(
    val taskRepository: TaskRepository,
    val goalRepository: GoalRepository,
) {

    @Transactional
    fun create(currentUserId: Long, createTaskRequest: CreateTaskRequest): CreateTaskResponse {
        val goal = goalRepository.getById(createTaskRequest.goalId)
        validateCurrentUserIsGoalOwner(currentUserId, goal)

        val task = Task.createTask(goal, createTaskRequest.description)
        taskRepository.save(task)
        return CreateTaskResponse(task.id!!, task.description)
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

    private fun validateCurrentUserIsGoalOwner(currentUserId: Long, goal: Goal) {
        if (currentUserId != goal.lifeMap.user.id) {
            throw SecurityException()
        }
    }
}
