package com.ssu.commerce.auth.domain.type

enum class TransactionType(val plus: Boolean) {
    CHARGE(true),
    ORDER(false),
    REWARD(true)
}
