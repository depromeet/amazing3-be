package io.raemian.api.auth.controller

import io.raemian.api.auth.controller.request.UpdateUserRequest
import io.raemian.api.user.controller.response.UserResponse
import io.raemian.api.auth.domain.CurrentUser
import io.raemian.api.auth.service.AuthService
import io.raemian.api.lifemap.LifeMapService
import io.raemian.api.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
    private val lifeMapService: LifeMapService,
) {

}
