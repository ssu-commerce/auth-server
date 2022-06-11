package com.ssu.commerce.auth

import com.ssu.commerce.core.configs.EnableSsuCommerceCore
import com.ssu.commerce.vault.config.EnableSsuCommerceVault
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableSsuCommerceVault
@EnableSsuCommerceCore
@SpringBootApplication
class AuthApplication

fun main(args: Array<String>) {
    runApplication<AuthApplication>(*args)
}
