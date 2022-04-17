package com.ssu.commerce.auth.controller

import com.ssu.commerce.auth.service.AuthService
import com.ssu.commerce.core.security.AuthInfo
import com.ssu.commerce.core.security.Authenticated
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

    @GetMapping("/auth-info")
    fun test(@Authenticated authInfo:AuthInfo): AuthInfo {
        return authInfo
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