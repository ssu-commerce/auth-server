package com.ssu.commerce.auth.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PointAccountRepository : JpaRepository<PointAccount, Long> {
    fun findByAccount_AccountId(accountId: UUID): PointAccount
}
