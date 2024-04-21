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
    private val pointAccountRepository: PointAccountRepository
) {
    fun signIn(req: SignInRequest): JwtTokenDto {
        val user = accountRepository.findByEmail(req.email) ?: throw SignInFailedException()
        verifyPassword(user.password, req.password)
        return generateAccessToken(user.accountId!!, user.email, user.roles)
    }

    private fun verifyPassword(userPassword: String, requestedPassword: String) {
        if (!passwordEncoder.matches(requestedPassword, userPassword)) throw SignInFailedException()
    }

    fun signUp(req: SignUpRequest) {
        val userRole = verifyAccountAndGiveRole(req)
        val account = accountRepository.save(
            Account(
                email = req.email,
                nickName = req.nickName,
                password = passwordEncoder.encode(req.password),
                roles = userRole
            )
        )
        pointAccountRepository.save(account.createPointAccount())
    }

    private fun verifyAccountAndGiveRole(req: SignUpRequest): MutableSet<UserRole> {
        // TODO 사용자 유형에 따른 롤관리를 어떻게 해야할지 정해지면 내용 추가
        val userRole = mutableSetOf(UserRole.ROLE_USER)
        return userRole
    }

    private fun generateAccessToken(userId: UUID, userName: String, roles: Set<UserRole>): JwtTokenDto =
        jwtTokenProvider.generateToken(userId, userName, roles, accessTokenValidMilSecond)
}
