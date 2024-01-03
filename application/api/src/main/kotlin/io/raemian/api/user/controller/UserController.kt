package io.raemian.api.user.controller

import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.user.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {

    @Operation(summary = "User의 인생 지도 공개 여부를 수정하는 API입니다.")
    @PatchMapping("/publication")
    fun updateIsGoalsPublic(
        @AuthenticationPrincipal currentUser: CurrentUser,
        @RequestBody updateIsGoalsPublicRequest: UpdateIsGoalsPublicRequest,
    ): ResponseEntity<Unit> {
        userService.updateGoalsPublic(currentUser.id, updateIsGoalsPublicRequest)
        return ResponseEntity.ok().build()
    }
}
