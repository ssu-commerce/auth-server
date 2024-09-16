package com.ssu.commerce.auth.domain

import com.ssu.commerce.auth.domain.type.PointAccountStatus
import com.ssu.commerce.auth.domain.type.TransactionType
import com.ssu.commerce.auth.exception.InvalidBalanceException
import com.ssu.commerce.auth.exception.PointAccountInactiveException
import com.ssu.commerce.core.jpa.BaseTimeEntity
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@DynamicUpdate
@Table(
    name = "point_account",
    uniqueConstraints = [
        UniqueConstraint(name = "uix_point_account", columnNames = ["account_id"])
    ]
)
data class PointAccount(

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    val pointAccountId: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "account_id")
    val account: Account,

    @Column(name = "balance", nullable = false)
    var balance: BigDecimal,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: PointAccountStatus,
) : BaseTimeEntity() {
    private fun checkAccountIsActive() {
        if (status != PointAccountStatus.ACTIVE) throw PointAccountInactiveException()
    }

    fun createPointTransaction(req: PointTransactionRequest): PointTransaction {
        checkAccountIsActive()
        val transaction = PointTransaction(
            pointAccount = this,
            transactionId = req.transactionId,
            transactionType = req.transactionType,
            beforeBalance = balance,
            transactionAmount = req.transactionAmount,
            afterBalance = balance.calculateAmount(req.transactionAmount, req.transactionType).validateBalance(),
            description = req.description,
            approvedAt = LocalDateTime.now()
        )
        balance = transaction.afterBalance
        return transaction
    }

    private fun BigDecimal.calculateAmount(requestAmount: BigDecimal, type: TransactionType) =
        if (type.plus) plus(requestAmount)
        else minus(requestAmount)

    private fun BigDecimal.validateBalance() =
        if (this.signum() < 1) throw InvalidBalanceException()
        else this
}
