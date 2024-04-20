package com.ssu.commerce.auth.service

import com.ssu.commerce.auth.domain.AccountRepository
import com.ssu.commerce.auth.domain.PointAccount
import com.ssu.commerce.auth.domain.PointAccountRepository
import com.ssu.commerce.auth.domain.PointTransactionRepository
import com.ssu.commerce.auth.domain.PointTransactionRequest
import com.ssu.commerce.auth.domain.PointTransactionResponse
import com.ssu.commerce.auth.domain.type.TransactionType
import com.ssu.commerce.auth.exception.AccountNotFoundException
import com.ssu.commerce.auth.exception.PointAccountInactiveException
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID
import javax.persistence.LockModeType

@Service
@Transactional
class PointService(
    private val accountRepository: AccountRepository,
    private val pointAccountRepository: PointAccountRepository,
    private val pointTransactionRepository: PointTransactionRepository
) {
    fun createPointAccount(userId: String): PointAccount {
        val account = accountRepository.findByUserId(userId) ?: throw AccountNotFoundException()
        return pointAccountRepository.save(account.createPointAccount())
    }

    @Transactional(readOnly = false)
    fun getPointTransaction(userId: UUID, fromDateTime: LocalDate, toDateTime: LocalDate) =
        pointTransactionRepository.findAllByApprovedAtGreaterThanEqualAndApprovedAtLessThan(
            fromDateTime.atStartOfDay(),
            toDateTime.plusDays(1).atStartOfDay()
        )

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    fun createTransaction(req: PointTransactionRequest): PointTransactionResponse {
        return when (req.transactionType) {
            TransactionType.CHARGE ->
                (pointAccountRepository.findByAccountId(req.accountTo) ?: throw PointAccountInactiveException())
                    .run { pointTransactionRepository.save(createPointTransaction(req)) }
                    .run { PointTransactionResponse(this) }

            TransactionType.ORDER -> TODO("아직 구현되지 않았습니다.")
            TransactionType.REWARD -> TODO("아직 구현되지 않았습니다.")
        }
    }
}
