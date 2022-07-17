package com.ssu.commerce.auth.exception

import com.ssu.commerce.core.exception.SsuCommerceException
import org.springframework.http.HttpStatus

class SignInFailedException(message: String? = null) :
    SsuCommerceException(HttpStatus.BAD_REQUEST, "AUTH-1", message ?: "로그인에 실패했습니다.")

class RefreshFailedException(message: String? = null) :
    SsuCommerceException(HttpStatus.BAD_REQUEST, "AUTH-2", message ?: "다른 클라이언트에서 로그인이 감지되었습니다. 재로그인해주세요.")

class EmailVerificationNotCompletedException(message: String? = null) :
    SsuCommerceException(HttpStatus.BAD_REQUEST, "AUTH-3", message ?: "이메일 인증이 되지 않은 계정입니다.")
