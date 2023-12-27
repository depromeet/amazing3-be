package io.raemian.adminapi.sticker.controller

import io.raemian.adminapi.sticker.StickerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sticker")
class StickerController(
    private val stickerService: StickerService,
) {

    @GetMapping
    fun findAll() {
    }
}
