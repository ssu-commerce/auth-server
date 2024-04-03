package com.ssu.commerce.auth.controller.user

import com.ssu.commerce.auth.service.GetUserInfoResponse
import com.ssu.commerce.auth.service.UserService
import com.ssu.commerce.core.security.user.SsuCommerceAuthenticatedPrincipal
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getUserInfo(@AuthenticationPrincipal @Parameter(hidden = true) principal: SsuCommerceAuthenticatedPrincipal): GetUserInfoResponse {
        return userService.getUserInfo(principal.userId)
    }
}
