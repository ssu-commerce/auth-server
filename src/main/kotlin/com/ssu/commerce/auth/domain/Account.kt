package com.ssu.commerce.auth.domain

import com.ssu.commerce.auth.domain.type.PointAccountStatus
import com.ssu.commerce.auth.service.SessionTokens
import com.ssu.commerce.core.security.UserRole
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val accountId: Long? = null,

    @Column(nullable = false, unique = true)
    val userId: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    private var refreshToken: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<UserRole>
) {
    fun checkEmailVerifiedUser() {
        // TODO 이메일 인증 진행시 구현
    }
    fun createPointAccount(): PointAccount {
        checkEmailVerifiedUser()
        return PointAccount(
            account = this,
            balance = BigDecimal.ZERO,
            status = PointAccountStatus.ACTIVE
        )
    }

    fun updateRefreshToken(tokens: SessionTokens): SessionTokens {
        refreshToken = tokens.refreshToken.token
        return tokens
    }
}
