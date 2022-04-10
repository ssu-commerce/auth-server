package com.ssu.commerce.auth.service

import com.ssu.commerce.auth.controller.SignInRequest
import com.ssu.commerce.auth.controller.SignUpRequest
import com.ssu.commerce.auth.domain.Account
import com.ssu.commerce.auth.domain.AccountRepository
import com.ssu.commerce.core.exception.NotFoundException
import com.ssu.commerce.core.security.JwtTokenDto
import com.ssu.commerce.core.security.JwtTokenProvider
import com.ssu.commerce.core.security.UserRole
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    @Value("\${jwt.accessTokenValidMS}") private val accessTokenValidMilSecond: Long = 0,
    @Value("\${jwt.refreshTokenValidMS}") private val refreshTokenValidMilSecond: Long = 0
) {
    fun signIn(req: SignInRequest): JwtTokenDto {
        val account = accountRepository.findByUserId(req.id) ?: throw NotFoundException()
        account.password.verifyPassword(req.password)
        return generateAccessToken(account.userId, account.roles)
    }

    private fun String.verifyPassword(requestedPassword:String) {
        if (!passwordEncoder.matches(requestedPassword, this)) throw NotFoundException()
    }

    fun signUp(req: SignUpRequest): JwtTokenDto {
        verifyAccount(req)
        val account = accountRepository.save(
            Account(
                userId = req.id,
                password = passwordEncoder.encode(req.password),
                roles = mutableSetOf(UserRole.ROLE_USER)
            )
        )
        return generateAccessToken(account.userId, account.roles)
    }

    private fun verifyAccount(req: SignUpRequest) {
    }

    private fun generateAccessToken(userId: String, roles: Set<UserRole>): JwtTokenDto =
        jwtTokenProvider.generateToken(userId, roles, accessTokenValidMilSecond)

    private fun generateRefreshToken(userId: String, roles: Set<UserRole>): JwtTokenDto =
        jwtTokenProvider.generateToken(userId, roles, refreshTokenValidMilSecond)
}