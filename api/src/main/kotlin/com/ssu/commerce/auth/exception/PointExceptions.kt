package com.ssu.commerce.auth.exception

import com.ssu.commerce.core.error.SsuCommerceException
import org.springframework.http.HttpStatus

class PointAccountInactiveException(message: String? = null) :
    SsuCommerceException(HttpStatus.BAD_REQUEST.value(), "POINT-1", message ?: "사용할수없는 포인트 계좌입니다.")
class InvalidBalanceException(message: String? = null) :
    SsuCommerceException(HttpStatus.BAD_REQUEST.value(), "POINT-2", message ?: "유효하지 않은 잔액값입니다.")
