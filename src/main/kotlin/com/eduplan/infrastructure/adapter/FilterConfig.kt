package com.eduplan.infrastructure.adapter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered

@Configuration
class FilterConfig {

    @Bean
    fun mdcFilterRegistration(mdcFilter: MdcFilter): FilterRegistrationBean<MdcFilter> {
        return FilterRegistrationBean(mdcFilter).apply {
            addUrlPatterns("/*")
            order = Ordered.HIGHEST_PRECEDENCE
        }
    }
}