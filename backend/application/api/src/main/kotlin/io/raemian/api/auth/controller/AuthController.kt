package io.raemian.api.auth.controller

import io.raemian.api.auth.service.AuthService
import io.raemian.api.lifemap.service.LifeMapService
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
    private val lifeMapService: LifeMapService,
)
