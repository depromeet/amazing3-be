package io.raemian.api.tag.controller

import io.raemian.api.support.response.ApiResponse
import io.raemian.api.tag.TagService
import io.raemian.api.tag.controller.response.TagResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tag")
class TagController(
    private val tagService: TagService,
) {

    @Operation(summary = "태그 전체 조회 API")
    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<List<TagResponse>>> =
        ResponseEntity.ok(
            ApiResponse.success(tagService.findAll()),
        )
}
