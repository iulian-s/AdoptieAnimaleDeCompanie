package com.example.adoptie.controller

import com.example.adoptie.config.DetaliiUtilizator
import com.example.adoptie.config.LogActiuneInterceptor
import com.example.adoptie.config.LogConfig
import com.example.adoptie.model.Localitate
import com.example.adoptie.model.Utilizator
import com.example.adoptie.repository.LogActiuneRepository
import com.example.adoptie.service.DetaliiUtilizatorService
import com.example.adoptie.service.JwtService
import com.example.adoptie.service.UtilizatorService
import com.example.adoptie.service.anyKotlin
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [AuthController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [LogConfig::class]
        )
    ]
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var utilizatorService: UtilizatorService

    @MockitoBean
    private lateinit var jwtService: JwtService

    @MockitoBean
    private lateinit var detaliiUtilizatorService: DetaliiUtilizatorService

    @MockitoBean
    private lateinit var logActiuneInterceptor: LogActiuneInterceptor

    @MockitoBean
    private lateinit var authenticationManager: AuthenticationManager

    @MockitoBean
    private lateinit var logActiuneRepository: LogActiuneRepository



    @Test
    fun loginTest() {
        val userDetails = mock(DetaliiUtilizator::class.java)
        `when`(detaliiUtilizatorService.loadUserByUsername("test_user")).thenReturn(userDetails)
        `when`(jwtService.generateToken(userDetails)).thenReturn("token")

        val json = """
            {
            "username": "test_user",
            "parola": "pass"
            }""".trimIndent()

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").value("token"))
    }

//    @Test
//    fun registerTest(){
//        val userDetails = mock(DetaliiUtilizator::class.java)
//        val utilizator = Utilizator(id = 1L, username = "test_user", parola = "pass",localitate = mock(Localitate::class.java))
//        `when`(utilizatorService.inregistrare(anyKotlin())).thenReturn(utilizator)
//        `when`(detaliiUtilizatorService.loadUserByUsername("test_user")).thenReturn(userDetails)
//        `when`(jwtService.generateToken(userDetails)).thenReturn("token")
//
//        val json = """
//            {
//            "username": "test_user",
//            "parola": "pass"
//            }""".trimIndent()
//        mockMvc.perform(
//            post("/api/auth/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(json)
//        )
//            .andExpect(status().isOk)
//            .andExpect(jsonPath("$.token").value("token"))
//    }

}