package com.ssu.commerce.auth.controller.auth

import com.ssu.commerce.auth.service.AuthService
import com.ssu.commerce.core.security.user.SsuCommerceAuthenticatedPrincipal
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/sign-in")
    fun signIn(@RequestBody signInRequest: SignInRequest) =
        authService.signIn(signInRequest)

    @PostMapping("/sign-up")
    fun signUp(@RequestBody signUpRequest: SignUpRequest) =
        authService.signUp(signUpRequest)

    @GetMapping("/info")
    fun test(
        @AuthenticationPrincipal @Parameter(hidden = true) principal: SsuCommerceAuthenticatedPrincipal,
    ): SsuCommerceAuthenticatedPrincipal {
        return principal
    }
}

data class SignInRequest(
    val id: String,
    val password: String
)

data class SignUpRequest(
    val id: String,
    val password: String
)
