package com.ssu.commerce.auth.service

import com.ssu.commerce.auth.controller.auth.RefreshTokenRequest
import com.ssu.commerce.auth.controller.auth.SignInRequest
import com.ssu.commerce.auth.controller.auth.SignUpRequest
import com.ssu.commerce.auth.domain.Account
import com.ssu.commerce.auth.domain.AccountRepository
import com.ssu.commerce.auth.domain.PointAccountRepository
import com.ssu.commerce.auth.exception.RefreshFailedException
import com.ssu.commerce.auth.exception.SignInFailedException
import com.ssu.commerce.core.security.JwtTokenDto
import com.ssu.commerce.core.security.JwtTokenProvider
import com.ssu.commerce.core.security.UserRole
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    @Value("\${jwt.accessTokenValidMS}") private val accessTokenValidMilSecond: Long = 0,
    @Value("\${jwt.refreshTokenValidMS}") private val refreshTokenValidMilSecond: Long = 0,
    private val pointAccountRepository: PointAccountRepository
) {
    fun signIn(req: SignInRequest): SessionTokens =
        accountRepository.findByUserId(req.id)
            ?.apply { password.verifyPassword(req.password) }
            ?.apply { req.userRole?.let { if (!roles.contains(it)) throw SignInFailedException() } }
            ?.let { it.updateRefreshToken(issueTokens(it.userId, it.roles)) }
            ?: throw SignInFailedException()

    private fun String.verifyPassword(requestedPassword: String) {
        if (!passwordEncoder.matches(requestedPassword, this)) throw SignInFailedException()
    }

    fun signUp(req: SignUpRequest): SessionTokens {
        val userRole = verifyAccountAndGiveRole(req)
        val (accessToken, refreshToken) = issueTokens(req.id, userRole)
        val account = accountRepository.save(
            Account(
                userId = req.id,
                password = passwordEncoder.encode(req.password),
                roles = userRole,
                refreshToken = refreshToken.token
            )
        )
        pointAccountRepository.save(account.createPointAccount())
        return SessionTokens(accessToken, refreshToken)
    }

    private fun verifyAccountAndGiveRole(req: SignUpRequest): MutableSet<UserRole> {
        val userRole = mutableSetOf(UserRole.ROLE_USER)
        return userRole
    }

    fun refreshToken(req: RefreshTokenRequest): SessionTokens? {
        val userId = validateAccessTokenAndRefreshTokenPairIsValid(req.accessToken, req.refreshToken)
        return accountRepository.findByUserId(userId)
            ?.apply { checkRefreshTokenIsValid(getRefreshToken(), req.refreshToken, roles) }
            ?.let { it.updateRefreshToken(issueTokens(it.userId, it.roles)) }
            ?: throw SignInFailedException()
    }

    private fun validateAccessTokenAndRefreshTokenPairIsValid(accessToken: String, refreshToken: String): String {
        val accessTokenUserId = getUserIdFromAccessTokenIgnoreExpired(accessToken)
        val refreshTokenUserId = jwtTokenProvider.getUserIdFromToken(refreshToken)
        if (accessTokenUserId != refreshTokenUserId) throw SignInFailedException()
        return refreshTokenUserId
    }

    private fun checkRefreshTokenIsValid(accountRefreshToken: String, refreshToken: String, userRoles: Set<UserRole>) {
        if (!userRoles.contains(UserRole.ROLE_SERVER) && accountRefreshToken != refreshToken) throw RefreshFailedException()
    }

    private fun getUserIdFromAccessTokenIgnoreExpired(accessToken: String): String =
        try {
            jwtTokenProvider.getUserIdFromToken(accessToken)
        } catch (e: ExpiredJwtException) {
            e.claims["id"] as String
        }

    private fun issueTokens(userId: String, roles: Set<UserRole>) =
        SessionTokens(generateAccessToken(userId, roles), generateRefreshToken(userId, roles))

    private fun generateAccessToken(userId: String, roles: Set<UserRole>): JwtTokenDto =
        jwtTokenProvider.generateToken(userId, roles, accessTokenValidMilSecond)

    private fun generateRefreshToken(userId: String, roles: Set<UserRole>): JwtTokenDto =
        jwtTokenProvider.generateToken(userId, roles, refreshTokenValidMilSecond)
}

data class SessionTokens(
    val accessToken: JwtTokenDto,
    val refreshToken: JwtTokenDto
)
