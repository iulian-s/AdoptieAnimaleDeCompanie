package com.example.adoptie.config

import com.example.adoptie.service.DetaliiUtilizatorService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Clasa care gestioneaza functia de login si criptarea parolei
 */
@EnableWebSecurity
@Configuration
class ConfigSpringSecurity(
    private val jwtAuthFilter: JwtAuthFilter,
    private val userDetailsService: DetaliiUtilizatorService,

) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors{}
            .csrf{it.disable()}

            .authorizeHttpRequests {
                it.requestMatchers("/api/auth/**").permitAll() // deschide endpointuri pt logare si inregistrare
                it.requestMatchers("/api/animalute/**").permitAll()
                it.requestMatchers("/api/**").permitAll() //TREBUIE STEARSA NEAPARAT DUPA TESTARE
                it.requestMatchers("/imagini/**").permitAll()
                it.requestMatchers("/localitati/**").permitAll()
                it.requestMatchers("/utilizator/**").permitAll()
                it.requestMatchers("/logs").permitAll()

            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }


    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager

    /**
     * configurare cors ca sa permita requesturi facute de hostul frontendului la admin(3000 - react)
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

//    @Bean
//    fun webSecurityCustomizer(): WebSecurityCustomizer {
//        return WebSecurityCustomizer {
//            it.ignoring().requestMatchers("/imagini/**")
//        }
//    }

}