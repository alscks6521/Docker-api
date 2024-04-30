package com.daelim.lotto.infra

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Value("\${swagger.url}")
    private val swaggerURL: String? = null

    @Value("\${swagger.description}")
    private val swaggerDescription: String? = null

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(Info().title("LOTTO API").version("v1.0"))
            .servers(
                listOf(
                    Server().url(swaggerURL)
                        .description(swaggerDescription),
                )
            )
    }
}
