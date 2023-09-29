package com.ssu.commerce.auth.controller.point

import com.ssu.commerce.auth.domain.PointTransactionProjection
import com.ssu.commerce.auth.domain.PointTransactionRequest
import com.ssu.commerce.auth.domain.PointTransactionResponse
import com.ssu.commerce.auth.service.PointService
import com.ssu.commerce.core.security.user.SsuCommerceAuthenticatedPrincipal
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/point")
class PointController(
    private val pointService: PointService
) {
    @GetMapping("/history")
    fun getPointTransaction(
        @AuthenticationPrincipal @Parameter(hidden = true) principal: SsuCommerceAuthenticatedPrincipal,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) fromDateTime: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) toDateTime: LocalDate
    ): List<PointTransactionProjection> =
        pointService.getPointTransaction(principal.userId, fromDateTime, toDateTime)

    @PostMapping("/account")
    fun createPointAccount(
        @AuthenticationPrincipal @Parameter(hidden = true) principal: SsuCommerceAuthenticatedPrincipal,
    ) = pointService.createPointAccount(principal.name)

    @PostMapping("/transaction")
    fun createTransaction(
        @RequestBody req: PointTransactionRequest
    ): PointTransactionResponse = pointService.createTransaction(req)
}
