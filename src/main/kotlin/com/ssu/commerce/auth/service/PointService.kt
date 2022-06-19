package com.ssu.commerce.auth.service

import com.ssu.commerce.auth.domain.AccountRepository
import com.ssu.commerce.auth.domain.PointAccount
import com.ssu.commerce.auth.domain.PointAccountRepository
import com.ssu.commerce.auth.domain.PointTransactionRepository
import com.ssu.commerce.auth.domain.PointTransactionRequest
import com.ssu.commerce.auth.domain.PointTransactionResponse
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import javax.persistence.LockModeType

@Service
@Transactional
class PointService(
    private val accountRepository: AccountRepository,
    private val pointAccountRepository: PointAccountRepository,
    private val PointTransactionRepository: PointTransactionRepository
) {
    fun createPointAccount(userId: String): PointAccount =
        accountRepository.findByUserId(userId)!!
            .let { pointAccountRepository.save(it.createPointAccount()) }

    @Transactional(readOnly = false)
    fun getPointTransaction(userId: String, fromDateTime: LocalDate, toDateTime: LocalDate) =
        PointTransactionRepository.findAllByApprovedAtGreaterThanEqualAndApprovedAtLessThan(
            fromDateTime.atStartOfDay(),
            toDateTime.plusDays(1).atStartOfDay()
        )

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    fun createTransaction(req: PointTransactionRequest): PointTransactionResponse =
        pointAccountRepository.findByAccount_AccountId(req.accountId)
            .run { PointTransactionRepository.save(createPointTransaction(req)) }
            .run { PointTransactionResponse(this) }
}
