package com.ssu.commerce.auth.exception

import com.ssu.commerce.core.error.SsuCommerceException
import org.springframework.http.HttpStatus

class SignInFailedException(message: String? = null) :
    SsuCommerceException(HttpStatus.BAD_REQUEST.value(), "AUTH-1", message ?: "로그인에 실패했습니다.")

class EmailVerificationNotCompletedException(message: String? = null) :
    SsuCommerceException(HttpStatus.BAD_REQUEST.value(), "AUTH-2", message ?: "이메일 인증이 되지 않은 계정입니다.")

class AccountNotFoundException(message: String? = null) :
    SsuCommerceException(HttpStatus.BAD_REQUEST.value(), "AUTH-3", message ?: "존재하지 않는 계정입니다.")
