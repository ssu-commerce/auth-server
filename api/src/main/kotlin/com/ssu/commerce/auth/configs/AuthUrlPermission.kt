package com.ssu.commerce.auth.configs

import com.ssu.commerce.core.security.config.UrlPermissionFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Primary
@Component
class AuthUrlPermission : UrlPermissionFilter {
    override fun urlPermissions(authorizeRequests: ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry): ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry =
        authorizeRequests
            .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
            .anyRequest().authenticated()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
