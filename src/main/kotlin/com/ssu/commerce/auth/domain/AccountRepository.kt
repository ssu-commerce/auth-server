package com.ssu.commerce.auth.domain

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository: JpaRepository<Account, Long> {
    fun findByUserId(userId: String): Account?
}