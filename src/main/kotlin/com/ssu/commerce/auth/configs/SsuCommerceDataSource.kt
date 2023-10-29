package com.ssu.commerce.auth.configs

import com.ssu.commerce.core.jpa.config.AbstractDataSourceProperties
import com.ssu.commerce.core.jpa.config.DataSourceConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.vault.annotation.VaultPropertySource
import org.springframework.vault.annotation.VaultPropertySources
@Configuration
@Profile("!test")
class SsuCommerceDataSource(dataSourceProperties: AbstractDataSourceProperties) :
    DataSourceConfig(dataSourceProperties)

/**
 * dev production
 */

@Configuration
@Profile("!test")
@VaultPropertySources(
    VaultPropertySource(value = ["ssu-commerce-auth/\${spring.profiles.active:local}"], propertyNamePrefix = "ssu-commerce-auth."),
    VaultPropertySource(value = ["ssu-commerce-auth/dev"], propertyNamePrefix = "ssu-commerce-auth.")
)
class DataSourceProperties : AbstractDataSourceProperties {
    override var projectName: String = "Auth"
    @Value("\${ssu-commerce-auth.dataSource}")
    override lateinit var dataSource: String
    @Value("\${ssu-commerce-auth.userId}")
    override lateinit var userId: String
    @Value("\${ssu-commerce-auth.password}")
    override lateinit var password: String
    @Value("\${ssu-commerce-auth.driverClassName}")
    override lateinit var driverClassName: String
}
