package io.raemian.api.task.controller

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.goal.controller.toUri
import io.raemian.api.task.TaskService
import io.raemian.api.task.controller.request.CreateTaskRequest
import io.raemian.api.task.controller.request.RewriteTaskRequest
import io.raemian.api.task.controller.request.UpdateTaskCompletionRequest
import io.raemian.api.task.controller.response.CreateTaskResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/task")
class TaskController(
    private val taskService: TaskService,
) {

    @PostMapping
    fun create(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody createTaskRequest: CreateTaskRequest,
    ): ResponseEntity<CreateTaskResponse> {
        val response = taskService.create(currentUser.id, createTaskRequest)
        return ResponseEntity.created("/task/${response.id}".toUri())
            .body(response)
    }

    @PatchMapping("/{taskId}/description")
    fun rewrite(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @PathVariable("taskId") taskId: Long,
        @RequestBody rewriteTaskRequest: RewriteTaskRequest,
    ): ResponseEntity<Unit> {
        taskService.rewrite(currentUser.id, taskId, rewriteTaskRequest)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{taskId}/isDone")
    fun updateTaskCompletion(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @PathVariable("taskId") taskId: Long,
        @RequestBody updateTaskCompletionRequest: UpdateTaskCompletionRequest,
    ): ResponseEntity<Unit> {
        taskService.updateTaskCompletion(currentUser.id, taskId, updateTaskCompletionRequest)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{taskId}")
    fun updateTaskCompletion(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @PathVariable("taskId") taskId: Long,
    ): ResponseEntity<Unit> {
        taskService.delete(currentUser.id, taskId)
        return ResponseEntity.noContent().build()
    }
}
