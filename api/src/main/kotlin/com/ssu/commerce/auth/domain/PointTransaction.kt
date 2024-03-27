package com.ssu.commerce.auth.domain

import com.ssu.commerce.auth.domain.type.TransactionType
import com.ssu.commerce.core.jpa.BaseTimeEntity
import org.hibernate.annotations.DynamicUpdate
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@DynamicUpdate
@Table(
    name = "point_transaction",
    uniqueConstraints = [
        UniqueConstraint(name = "uix_transaction", columnNames = ["transaction_id", "transaction_type"])
    ]
)
data class PointTransaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val pointTransactionId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "point_account_id")
    val pointAccount: PointAccount,

    @Column(name = "transaction_id", nullable = false)
    val transactionId: Long,

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val transactionType: TransactionType,

    @Column(name = "before_balance", nullable = false)
    val beforeBalance: BigDecimal,

    @Column(name = "transaction_amount", nullable = false)
    val transactionAmount: BigDecimal,

    @Column(name = "after_balance", nullable = false)
    val afterBalance: BigDecimal,

    @Column(name = "description")
    val description: String,

    @Column(name = "approved_at", nullable = false)
    val approvedAt: LocalDateTime,

    @Column(name = "reconciled", nullable = false)
    val isReconciled: Boolean = false
) : BaseTimeEntity()

data class PointTransactionRequest(
    val accountFrom: UUID,
    val accountTo: UUID,
    val transactionId: Long,
    val transactionType: TransactionType,
    val transactionAmount: BigDecimal,
    val description: String
)

data class PointTransactionResponse(
    val pointTransactionId: Long,
    val accountId: UUID,
    val transactionId: Long,
    val transactionType: TransactionType,
    val transactionAmount: BigDecimal,
    val afterBalance: BigDecimal,
    val description: String,
    val approvedAt: LocalDateTime
) {
    constructor(transaction: PointTransaction) : this(
        pointTransactionId = transaction.pointTransactionId!!,
        accountId = transaction.pointAccount.account.accountId!!,
        transactionId = transaction.transactionId,
        transactionType = transaction.transactionType,
        transactionAmount = transaction.transactionAmount,
        afterBalance = transaction.afterBalance,
        description = transaction.description,
        approvedAt = transaction.approvedAt
    )
}
