package io.raemian.api.task

import io.raemian.api.task.controller.CreateTaskResponse
import io.raemian.api.task.controller.request.DeleteTaskRequest
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
    fun create(goalId: Long, description: String): CreateTaskResponse {
        val goal = goalRepository.getById(goalId)
        val task = Task.createTask(goal, description)
        taskRepository.save(task)
        return CreateTaskResponse(task.id!!)
    }

    @Transactional
    fun rewrite(taskId: Long, newDescription: String) {
        val task = taskRepository.getById(taskId)
        task.rewrite(newDescription)
        taskRepository.save(task)
    }

    @Transactional
    fun updateTaskCompletion(taskId: Long, isDone: Boolean) {
        val task = taskRepository.getById(taskId)
        task.updateTaskCompletion(isDone)
        taskRepository.save(task)
    }

    @Transactional
    fun delete(userId: Long, deleteTaskRequest: DeleteTaskRequest) {
        val task = taskRepository.getById(deleteTaskRequest.taskId)
        validateTaskIsUsers(userId, task)
        taskRepository.delete(task)
    }

    private fun validateTaskIsUsers(userId: Long, task: Task) {
        if (userId != task.goal.user.id) {
            throw SecurityException()
        }
    }
}
