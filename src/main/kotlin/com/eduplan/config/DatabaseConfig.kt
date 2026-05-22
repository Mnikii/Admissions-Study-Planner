package com.eduplan.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJpaRepositories(basePackages = ["com.eduplan.infrastructure.repository"])
@EnableJpaAuditing
@EnableCaching
@EnableTransactionManagement
class DatabaseConfig
