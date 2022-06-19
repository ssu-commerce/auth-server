package com.ssu.commerce.auth.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PointAccountRepository : JpaRepository<PointAccount, Long> {
    fun findByAccount_AccountId(accountId: Long): PointAccount
}
