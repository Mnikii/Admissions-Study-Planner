package com.eduplan.config

import com.eduplan.domain.service.DemoFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Конфигурация Domain слоя
 *
 * Регистрируем domain сервисы как Spring beans.
 */
@Configuration
class DomainConfig {

    @Bean
    fun demoFactory(): DemoFactory {
        return DemoFactory()
    }
}
