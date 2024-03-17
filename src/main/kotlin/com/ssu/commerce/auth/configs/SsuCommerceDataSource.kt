package com.ssu.commerce.auth.configs

import com.ssu.commerce.core.jpa.config.AbstractDataSourceProperties
import com.ssu.commerce.core.jpa.config.DataSourceConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class SsuCommerceDataSource(dataSourceProperties: AbstractDataSourceProperties) :
    DataSourceConfig(dataSourceProperties)

/**
 * dev production
 */

@Configuration
@Profile("!test")
@ConfigurationProperties("ssu-commerce-auth")
class DataSourceProperties : AbstractDataSourceProperties {
    override lateinit var dataSource: String
    override lateinit var userId: String
    override lateinit var password: String
    override lateinit var driverClassName: String
}
