package com.ssu.commerce.auth.domain

import com.ssu.commerce.auth.domain.type.TransactionType
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal
import java.time.LocalDateTime

interface PointTransactionRepository : JpaRepository<PointTransaction, Long> {
    fun findAllByApprovedAtGreaterThanEqualAndApprovedAtLessThan(to: LocalDateTime, from: LocalDateTime): List<PointTransactionProjection>
}

data class PointTransactionProjection(
    val PointTransactionId: Long,
    val transactionId: Long,
    val transactionType: TransactionType,
    val beforeBalance: BigDecimal,
    val transactionAmount: BigDecimal,
    val afterBalance: BigDecimal,
    val description: String,
    val approvedAt: LocalDateTime
)
