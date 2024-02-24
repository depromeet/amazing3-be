package io.raemian.api.emoji.controller

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.emoji.EmojiService
import io.raemian.api.emoji.controller.request.ReactEmojiRequest
import io.raemian.api.emoji.controller.request.RemoveEmojiRequest
import io.raemian.api.emoji.controller.response.EmojiResponse
import io.raemian.api.emoji.controller.response.ReactedEmojisResponse
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
@RequestMapping("/emoji")
class EmojiController(
    private val emojiService: EmojiService,
) {

    @Operation(summary = "이모지 전체 조회 API")
    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<List<EmojiResponse>>> =
        ResponseEntity.ok(
            ApiResponse.success(emojiService.findAll()),
        )

    @Operation(summary = "Goal에 반응된 이모지와 유저 정보 전체 조회 API")
    @GetMapping("/{goalId}")
    fun findAllReactedEmojisAtGoal(@PathVariable goalId: Long): ResponseEntity<ApiResponse<ReactedEmojisResponse>> {
        val response = emojiService.findAllReactedEmojisByGoalId(goalId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @Operation(summary = "Goal에 이모지 반응하기 API")
    @PostMapping("/{goalId}")
    fun react(
        @RequestBody reactEmojiRequest: ReactEmojiRequest,
        @PathVariable goalId: Long,
        @AuthenticationPrincipal currentUser: CurrentUser,
    ): ResponseEntity<ApiResponse<Unit>> {
        emojiService.react(reactEmojiRequest.emojiId, goalId, currentUser.id)
        return ResponseEntity.ok(ApiResponse.success())
    }

    @Operation(summary = "Goal에 반응한 이모지 삭제 API")
    @DeleteMapping("/{goalId}")
    fun remove(
        @RequestBody removeEmojiRequest: RemoveEmojiRequest,
        @PathVariable goalId: Long,
        @AuthenticationPrincipal currentUser: CurrentUser,
    ): ResponseEntity<ApiResponse<Unit>> {
        emojiService.remove(removeEmojiRequest.emojiId, goalId, currentUser.id)
        return ResponseEntity.ok(ApiResponse.success())
    }
}
