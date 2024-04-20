package com.ssu.commerce.auth.domain

import com.ssu.commerce.auth.domain.QPointAccount.pointAccount
import com.ssu.commerce.core.jpa.querydsl.QuerydslJPARepositorySupport
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PointAccountRepository : JpaRepository<PointAccount, Long>, PointAccountQuerydslRepository

interface PointAccountQuerydslRepository {
    fun findByAccountId(accountId: UUID): PointAccount?
}

class PointAccountQuerydslRepositoryImpl : PointAccountQuerydslRepository,
    QuerydslJPARepositorySupport(PointAccount::class.java) {
    override fun findByAccountId(accountId: UUID): PointAccount? =
        queryFactory.select(pointAccount)
            .from(pointAccount)
            .where(pointAccount.account.accountId.eq(accountId))
            .fetchOne()
}
