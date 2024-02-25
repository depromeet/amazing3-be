package io.raemian.admin.emoji.controller

import io.raemian.admin.emoji.EmojiService
import io.raemian.admin.emoji.controller.request.CreateEmojiRequest
import io.raemian.admin.emoji.controller.request.UpdateEmojiRequest
import io.raemian.admin.emoji.controller.response.EmojiResponse
import io.raemian.admin.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

fun String.toUri(): URI = URI.create(this)

@RestController
@RequestMapping("/emoji")
class EmojiController(
    private val emojiService: EmojiService,
) {

    @Operation(summary = "이모지 생성 API")
    @PostMapping(consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun create(
        @ModelAttribute createEmojiRequest: CreateEmojiRequest,
    ): ResponseEntity<ApiResponse<EmojiResponse>> {
        val response = emojiService.create(createEmojiRequest)

        return ResponseEntity
            .created("/emoji/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @Operation(summary = "이모지 전체 조회 API")
    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<List<EmojiResponse>>> =
        ResponseEntity.ok(ApiResponse.success(emojiService.findAll()))

    @Operation(summary = "이모지 단건 조회 API")
    @GetMapping("/{emojiId}")
    fun find(@PathVariable emojiId: Long): ResponseEntity<ApiResponse<EmojiResponse>> =
        ResponseEntity.ok(ApiResponse.success(emojiService.find(emojiId)))

    @Operation(summary = "이모지 수정 API")
    @PatchMapping("/{emojiId}", consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun update(
        @PathVariable emojiId: Long,
        @ModelAttribute updateEmojiRequest: UpdateEmojiRequest,
    ): ResponseEntity<Unit> {
        emojiService.update(emojiId, updateEmojiRequest)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "이모지 삭제 API")
    @DeleteMapping("/{emojiId}")
    fun delete(
        @PathVariable emojiId: Long,
    ): ResponseEntity<Unit> {
        emojiService.delete(emojiId)
        return ResponseEntity.noContent().build()
    }
}
