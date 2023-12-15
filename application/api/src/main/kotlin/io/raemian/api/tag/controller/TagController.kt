package io.raemian.api.tag.controller

import io.raemian.api.tag.TagService
import io.raemian.api.tag.controller.response.TagResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tag")
class TagController(
    private val tagService: TagService,
) {

    @GetMapping
    fun findAll(): ResponseEntity<TagResponse> {
        return ResponseEntity.ok(tagService.findAll())
    }
}
