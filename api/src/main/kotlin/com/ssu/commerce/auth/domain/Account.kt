package com.ssu.commerce.auth.domain

import com.ssu.commerce.auth.domain.type.PointAccountStatus
import com.ssu.commerce.core.jpa.BaseEntity
import com.ssu.commerce.core.security.user.UserRole
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    val accountId: UUID? = null,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false, unique = true)
    val nickName: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<UserRole>
) : BaseEntity() {
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
}
