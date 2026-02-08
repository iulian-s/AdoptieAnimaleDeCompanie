package com.example.adoptie.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Configurarea interceptorului
 */
@Configuration
class LogConfig (private val loggingInterceptor: LogActiuneInterceptor): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggingInterceptor).addPathPatterns("/**")
    }
}