package com.ssu.commerce.auth.configs

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.vault.annotation.VaultPropertySource
import org.springframework.vault.annotation.VaultPropertySources
import javax.sql.DataSource

@Profile("!test")
@Configuration
class SsuCommerceDataSource(
    private val dataSourceProperties: DataSourceProperties
) {

    @Primary
    @Bean
    fun dataSource(): DataSource =
        DataSourceBuilder.create()
            .driverClassName(dataSourceProperties.driverClassName)
            .url(dataSourceProperties.dataSource)
            .username(dataSourceProperties.userId)
            .password(dataSourceProperties.password)
            .build()
            .also { LoggerFactory.getLogger(SsuCommerceDataSource::class.java).info("\uD83D\uDCC2 SSUCommerce Auth DataSource 접속이 되었습니다.") }
}

@Profile("!test")
@Configuration
@VaultPropertySources(
    VaultPropertySource(value = ["ssu-commerce-auth/\${spring.profiles.active:local}"], propertyNamePrefix = "ssu-commerce-auth."),
    VaultPropertySource(value = ["ssu-commerce-auth/dev"], propertyNamePrefix = "ssu-commerce-auth.")
)
class DataSourceProperties {
    @Value("\${ssu-commerce-auth.dataSource}")
    lateinit var dataSource: String
    @Value("\${ssu-commerce-auth.userId}")
    lateinit var userId: String
    @Value("\${ssu-commerce-auth.password}")
    lateinit var password: String
    @Value("\${ssu-commerce-auth.driverClassName}")
    lateinit var driverClassName: String
}