package io.raemian.admin.sticker.controller

import io.raemian.admin.sticker.StickerService
import io.raemian.admin.sticker.controller.request.CreateStickerRequest
import io.raemian.admin.sticker.controller.request.UpdateStickerRequest
import io.raemian.admin.sticker.controller.response.StickerResponse
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
@RequestMapping("/sticker")
class StickerController(
    private val stickerService: StickerService,
) {

    @Operation(summary = "스티커 생성 API")
    @PostMapping(consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun create(
        @ModelAttribute createStickerRequest: CreateStickerRequest,
    ): ResponseEntity<ApiResponse<StickerResponse>> {
        val response = stickerService.create(createStickerRequest)

        return ResponseEntity
            .created("/sticker/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @Operation(summary = "스티커 전체 조회 API")
    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<List<StickerResponse>>> =
        ResponseEntity.ok(ApiResponse.success(stickerService.findAll()))

    @Operation(summary = "스티커 수정 API")
    @PatchMapping("/{stickerId}", consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun update(
        @PathVariable stickerId: Long,
        @ModelAttribute updateStickerRequest: UpdateStickerRequest,
    ): ResponseEntity<Unit> {
        stickerService.update(stickerId, updateStickerRequest)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "스티커 삭제 API")
    @DeleteMapping("/{stickerId}")
    fun delete(
        @PathVariable stickerId: Long,
    ): ResponseEntity<Unit> {
        stickerService.delete(stickerId)
        return ResponseEntity.noContent().build()
    }
}
