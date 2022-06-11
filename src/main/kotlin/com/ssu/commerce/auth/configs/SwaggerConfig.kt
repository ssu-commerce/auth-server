package com.ssu.commerce.auth.configs

import com.ssu.commerce.core.configs.SwaggerDocsConfig
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig : SwaggerDocsConfig {
    override val basePackage: String = "com.ssu.commerce.auth"
    override val title: String = "Auth"
    override val version: String = "test"
}
