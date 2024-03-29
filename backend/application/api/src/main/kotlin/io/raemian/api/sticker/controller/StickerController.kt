package io.raemian.api.sticker.controller

import io.raemian.api.sticker.model.StickerResult
import io.raemian.api.sticker.service.StickerService
import io.raemian.api.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sticker")
class StickerController(
    private val stickerService: StickerService,
) {

    @Operation(summary = "스티커 전체 조회 API")
    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<List<StickerResult>>> =
        ResponseEntity.ok(
            ApiResponse.success(stickerService.findAll()),
        )
}
