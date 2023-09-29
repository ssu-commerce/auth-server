package com.ssu.commerce.auth.service

import com.ssu.commerce.auth.controller.auth.SignInRequest
import com.ssu.commerce.auth.controller.auth.SignUpRequest
import com.ssu.commerce.auth.domain.Account
import com.ssu.commerce.auth.domain.AccountRepository
import com.ssu.commerce.auth.domain.PointAccountRepository
import com.ssu.commerce.auth.exception.SignInFailedException
import com.ssu.commerce.core.security.jwt.JwtTokenDto
import com.ssu.commerce.core.security.jwt.JwtTokenProvider
import com.ssu.commerce.core.security.user.UserRole
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

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
            ?.let { it.updateRefreshToken(issueTokens(it.accountId!!, it.userId, it.roles)) }
            ?: throw SignInFailedException()

    private fun String.verifyPassword(requestedPassword: String) {
        if (!passwordEncoder.matches(requestedPassword, this)) throw SignInFailedException()
    }

    fun signUp(req: SignUpRequest): SessionTokens {
        val userRole = verifyAccountAndGiveRole(req)
        val account = accountRepository.save(
            Account(
                userId = req.id,
                password = passwordEncoder.encode(req.password),
                roles = userRole
            )
        )
        val sessionTokens = issueTokens(account.accountId!!, req.id, userRole)
        account.updateRefreshToken(sessionTokens)
        pointAccountRepository.save(account.createPointAccount())
        return SessionTokens(sessionTokens.accessToken, sessionTokens.refreshToken)
    }

    private fun verifyAccountAndGiveRole(req: SignUpRequest): MutableSet<UserRole> {
        val userRole = mutableSetOf(UserRole.ROLE_USER)
        return userRole
    }

    private fun issueTokens(userId: UUID, userName: String, roles: Set<UserRole>) =
        SessionTokens(generateAccessToken(userId, userName, roles), generateRefreshToken(userId, userName, roles))

    private fun generateAccessToken(userId: UUID, userName: String, roles: Set<UserRole>): JwtTokenDto =
        jwtTokenProvider.generateToken(userId, userName, roles, accessTokenValidMilSecond)

    private fun generateRefreshToken(userId: UUID, userName: String, roles: Set<UserRole>): JwtTokenDto =
        jwtTokenProvider.generateToken(userId, userName, roles, refreshTokenValidMilSecond)
}

data class SessionTokens(
    val accessToken: JwtTokenDto,
    val refreshToken: JwtTokenDto
)
