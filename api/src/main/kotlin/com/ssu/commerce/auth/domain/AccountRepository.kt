package com.ssu.commerce.auth.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AccountRepository : JpaRepository<Account, UUID> {
    fun findByEmail(userId: String): Account?
}
