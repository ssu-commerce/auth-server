package com.ssu.commerce.auth

import com.ssu.commerce.core.web.configs.EnableSsuCommerceCore
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableSsuCommerceCore
@SpringBootApplication
class AuthApplication

fun main(args: Array<String>) {
    runApplication<AuthApplication>(*args)
}
