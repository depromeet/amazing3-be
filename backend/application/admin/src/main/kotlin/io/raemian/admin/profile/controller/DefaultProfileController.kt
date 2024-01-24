package io.raemian.admin.profile.controller

import io.raemian.admin.profile.DefaultProfileService
import io.raemian.admin.profile.controller.request.CreateDefaultProfileRequest
import io.raemian.admin.profile.controller.request.UpdateDefaultProfileRequest
import io.raemian.admin.profile.controller.response.DefaultProfileResponse
import io.raemian.admin.sticker.controller.toUri
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
@RequestMapping("/profile/default")
class DefaultProfileController(
    private val defaultProfileService: DefaultProfileService,
) {
    @Operation(summary = "기본 프로필 생성 API")
    @PostMapping(consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun create(
        @ModelAttribute createDefaultProfileRequest: CreateDefaultProfileRequest,
    ): ResponseEntity<ApiResponse<DefaultProfileResponse>> {
        val response = defaultProfileService.create(createDefaultProfileRequest)

        return ResponseEntity
            .created("/profile/default/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @Operation(summary = "기본 프로필 전체 조회 API")
    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<List<DefaultProfileResponse>>> =
        ResponseEntity.ok(ApiResponse.success(defaultProfileService.findAll()))

    @Operation(summary = "기본 프로필 단건 조회 API")
    @GetMapping("/{defaultProflieId}")
    fun find(@PathVariable defaultProflieId: Long): ResponseEntity<ApiResponse<DefaultProfileResponse>> =
        ResponseEntity.ok(ApiResponse.success(defaultProfileService.find(defaultProflieId)))

    @Operation(summary = "기본 프로필 수정 API")
    @PatchMapping("/{defaultProflieId}", consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun update(
        @PathVariable defaultProflieId: Long,
        @ModelAttribute updateDefaultProfileRequest: UpdateDefaultProfileRequest,
    ): ResponseEntity<Unit> {
        defaultProfileService.update(defaultProflieId, updateDefaultProfileRequest)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "기본 프로필 삭제 API")
    @DeleteMapping("/{defaultProflieId}")
    fun delete(
        @PathVariable defaultProflieId: Long,
    ): ResponseEntity<Unit> {
        defaultProfileService.delete(defaultProflieId)
        return ResponseEntity.noContent().build()
    }
}
