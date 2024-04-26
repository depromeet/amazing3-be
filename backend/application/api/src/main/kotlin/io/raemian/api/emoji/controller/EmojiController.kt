package io.raemian.api.emoji.controller

import io.raemian.api.auth.model.CurrentUser
import io.raemian.api.emoji.model.EmojiResult
import io.raemian.api.emoji.model.ReactedEmojisResult
import io.raemian.api.emoji.service.EmojiService
import io.raemian.api.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EmojiController(
    private val emojiService: EmojiService,
) {

    @Operation(summary = "이모지 전체 조회 API")
    @GetMapping("/emoji")
    fun findAll(): ResponseEntity<ApiResponse<List<EmojiResult>>> =
        ResponseEntity.ok(
            ApiResponse.success(emojiService.findAll()),
        )

    @Operation(summary = "Goal에 반응된 이모지와 유저 정보 전체 조회 API")
    @GetMapping("/goal/{goalId}/emoji")
    fun findAllReactedEmojisAtGoal(
        @PathVariable goalId: Long,
        @AuthenticationPrincipal currentUser: CurrentUser,
    ): ResponseEntity<ApiResponse<ReactedEmojisResult>> {
        val response = emojiService.findAllReactedEmojisByGoalId(goalId, currentUser.id)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @Operation(summary = "Goal에 이모지 반응하기 API")
    @PostMapping("/goal/{goalId}/emoji/{emojiId}")
    fun react(
        @PathVariable goalId: Long,
        @PathVariable emojiId: Long,
        @AuthenticationPrincipal currentUser: CurrentUser,
    ): ResponseEntity<ApiResponse<Unit>> {
        emojiService.react(emojiId, goalId, currentUser.id)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @Operation(summary = "Goal에 반응한 이모지 삭제 API")
    @DeleteMapping("/goal/{goalId}/emoji/{emojiId}")
    fun remove(
        @PathVariable goalId: Long,
        @PathVariable emojiId: Long,
        @AuthenticationPrincipal currentUser: CurrentUser,
    ): ResponseEntity<ApiResponse<Unit>> {
        emojiService.remove(emojiId, goalId, currentUser.id)
        return ResponseEntity.ok(ApiResponse.success())
    }
}
