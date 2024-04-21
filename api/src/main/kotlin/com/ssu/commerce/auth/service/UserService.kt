package com.ssu.commerce.auth.service

import com.ssu.commerce.auth.domain.AccountRepository
import com.ssu.commerce.auth.exception.AccountNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    val accountRepository: AccountRepository
) {
    fun getUserInfo(userId: UUID): GetUserInfoResponse {
        val userAccount = accountRepository.findByIdOrNull(userId) ?: throw AccountNotFoundException()
        return GetUserInfoResponse(userAccount.email, userAccount.nickName)
    }
}

data class GetUserInfoResponse(
    val email: String,
    val nickName: String
)
