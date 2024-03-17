package io.raemian.admin.dashboard

import io.raemian.admin.dashboard.model.DashboardResult
import io.raemian.admin.dashboard.service.DashboardService
import io.raemian.admin.support.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dashboard")
class DashboardController(
    private val dashboardService: DashboardService,
) {
    @Operation(summary = "대시보드 통계 데이터 조회 API")
    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<DashboardResult>> =
        ResponseEntity.ok(ApiResponse.success(dashboardService.getDashBoard()))
}
