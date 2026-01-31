package com.example.adoptie

import jakarta.annotation.PostConstruct

import org.slf4j.LoggerFactory

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class DbPropertyLogger(
    @Value("\${DB_HOST}") private val host: String,
    @Value("\${DB_PORT}") private val port: String,
    @Value("\${DB_NAME}") private val name: String,
    @Value("\${DB_USER}") private val user: String,
    @Value("\${DB_PASSWORD}") private val password: String,
    @Value("\${JWT_SECRET}") private val jwtSecret: String
) {

    private val log = LoggerFactory.getLogger(DbPropertyLogger::class.java)

//    @PostConstruct
//    fun logProps() {
//        log.info("SPRING DB_HOST = {}", host)
//        log.info("SPRING DB_PORT = {}", port)
//        log.info("SPRING DB_NAME = {}", name)
//        log.info("SPRING DB_USER = {}", user)
//        log.info("SPRING DB_PASSWORD = {}", password)
//        log.info("JWT SECRET = {}", jwtSecret)
//    }
}
