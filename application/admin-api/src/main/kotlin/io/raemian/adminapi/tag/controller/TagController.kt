package io.raemian.adminapi.tag.controller

import io.raemian.adminapi.support.response.ApiResponse
import io.raemian.adminapi.tag.TagService
import io.raemian.adminapi.tag.controller.request.CreateTagRequest
import io.raemian.adminapi.tag.controller.request.UpdateTagRequest
import io.raemian.adminapi.tag.controller.response.TagResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

fun String.toUri(): URI = URI.create(this)

@RestController
@RequestMapping("/tag")
class TagController(
    private val tagService: TagService,
) {

    @PostMapping
    @Operation(summary = "태그 생성 API")
    fun create(
        @RequestBody createTagRequest: CreateTagRequest,
    ): ResponseEntity<ApiResponse<TagResponse>> {
        val response = tagService.create(createTagRequest)
        return ResponseEntity.created("/tag/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @GetMapping
    @Operation(summary = "태그 전체 조회 API")
    fun findAll(): ResponseEntity<ApiResponse<List<TagResponse>>> =
        ResponseEntity.ok(ApiResponse.success(tagService.findAll()))

    @PatchMapping("/{tagId}")
    @Operation(summary = "태그 수정 API")
    fun update(
        @PathVariable tagId: Long,
        @RequestBody updateTagRequest: UpdateTagRequest,
    ): ResponseEntity<ApiResponse<TagResponse>> =
        ResponseEntity.ok().body(ApiResponse.success(tagService.update(tagId, updateTagRequest)))

    @DeleteMapping("/{tagId}")
    @Operation(summary = "태그 삭제 API")
    fun delete(@PathVariable tagId: Long): ResponseEntity<Unit> {
        tagService.delete(tagId)
        return ResponseEntity.ok().build()
    }
}
