package com.example.adoptie.service

import com.example.adoptie.config.DetaliiUtilizator
import com.example.adoptie.model.Rol
import com.example.adoptie.model.Utilizator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.test.util.ReflectionTestUtils
import kotlin.test.Test

class JwtServiceTest {
    private lateinit var jwtService: JwtService


    @BeforeEach
    fun setup(){
        jwtService = JwtService()
        ReflectionTestUtils.setField(
            jwtService,
            "secretKey",
            "cheiesecretafoartelungadecelputin32caracterepentrutest"
        )
    }

    @Test
    fun `ar trebui sa genereze si sa extraga username corect`(){
        val user = Utilizator(username = "test_user", parola = "pass", rol = Rol.USER)
        val userDetails = DetaliiUtilizator(user)

        val token = jwtService.generateToken(userDetails)
        val extractedUsername = jwtService.extractUsername(token)
        assertNotNull(extractedUsername)
        assertNotNull(token)
        assertEquals(userDetails.username, extractedUsername)
    }

    @Test
    fun `isTokenValid ar trebui sa returneze true pt token valid`(){
        val user = Utilizator(username = "test_user", parola = "pass", rol = Rol.USER)
        val userDetails = DetaliiUtilizator(user)

        val token = jwtService.generateToken(userDetails)
        val isValid = jwtService.isTokenValid(token, userDetails)
        assertTrue(isValid)
    }

    @Test
    fun `isTokenValid ar trebui sa returneze fals daca username nu se potriveste`(){
        val user = Utilizator(username = "test_user", parola = "pass", rol = Rol.USER)
        val user2 = Utilizator(username = "test_user2", parola = "pass", rol = Rol.USER)
        val userDetails = DetaliiUtilizator(user)
        val userDetails2 = DetaliiUtilizator(user2)

        val token = jwtService.generateToken(userDetails)
        val isValid = jwtService.isTokenValid(token, userDetails2)
        assertFalse(isValid)
    }
}