package io.raemian.api.comment.controller

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.comment.CommentService
import io.raemian.api.comment.controller.request.WriteCommentRequest
import io.raemian.api.comment.controller.response.CommentsResponse
import io.raemian.api.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class CommentController(
    private val commentService: CommentService,
) {

    @Operation(summary = "Goal에 달린 댓글 전체 조회 API")
    @GetMapping("/goal/{goalId}/comment")
    fun findAllReactedEmojisAtGoal(
        @PathVariable goalId: Long,
        @AuthenticationPrincipal currentUser: CurrentUser,
    ): ResponseEntity<ApiResponse<CommentsResponse>> =
        ResponseEntity.ok(
            ApiResponse.success(commentService.findAllByGoalId(goalId, currentUser.id)),
        )

    @Operation(summary = "Goal 주인이 읽지 못한 새 댓글이 있는지 확인하는 API")
    @GetMapping("/goal/{goalId}/comment/new")
    fun isNewComment(
        @PathVariable goalId: Long,
    ): ResponseEntity<ApiResponse<Boolean>> =
        ResponseEntity.ok(
            ApiResponse.success(commentService.isNewComment(goalId)),
        )

    @Operation(summary = "Goal에 댓글을 작성하는 API")
    @PostMapping("/goal/{goalId}/comment")
    fun write(
        @PathVariable goalId: Long,
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody request: WriteCommentRequest,
    ): ResponseEntity<ApiResponse<Unit>> =
        ResponseEntity.ok(
            ApiResponse.success(commentService.write(goalId, currentUser.id, request)),
        )

    @Operation(summary = "댓글 삭제 API")
    @DeleteMapping("comment/{commentId}")
    fun delete(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal currentUser: CurrentUser,
    ): ResponseEntity<Unit> {
        commentService.delete(commentId, currentUser.id)
        return ResponseEntity.noContent().build()
    }
}
