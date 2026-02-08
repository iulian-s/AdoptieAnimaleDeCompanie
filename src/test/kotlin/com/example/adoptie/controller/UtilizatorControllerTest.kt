package com.example.adoptie.controller

import com.example.adoptie.config.LogActiuneInterceptor
import com.example.adoptie.config.LogConfig
import com.example.adoptie.dto.EditareUtilizatorDTO
import com.example.adoptie.dto.UtilizatorDTO
import com.example.adoptie.dto.toEntity
import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Localitate
import com.example.adoptie.model.Utilizator
import com.example.adoptie.service.DetaliiUtilizatorService
import com.example.adoptie.service.JwtService
import com.example.adoptie.service.UtilizatorService
import com.example.adoptie.service.anyKotlin
import com.google.gson.Gson
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
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [UtilizatorController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [LogConfig::class]
        )
    ]
)
@AutoConfigureMockMvc(addFilters = false)
class UtilizatorControllerTest {

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

    val locatie = Localitate(id = 3L)
    val utilizator = Utilizator(id = 1L, username = "test_user", localitate = locatie)

    @Test
    fun infoUtilizatorTest() {
        `when`(utilizatorService.infoUtilizator()).thenReturn(utilizator)
        mockMvc.perform(get("/api/utilizator/eu"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.username").value("test_user"))
    }

    @Test
    fun editareInfoUtilizatorTest() {
        val dto = EditareUtilizatorDTO(
            id = 1L,
            username = "test_user",
            parola = "test_parola_hashed",
            parolaVeche = "test_parola",
            parolaNoua = "parola_noua",
            nume = "Gelu",
            localitateId = 3L,
            telefon = "0123456789"
        ).toEntity(locatie, listOf<Anunt>(mock(Anunt::class.java)))


        val json = """
            {
            "id": "1",
            "username": "test_user",
            "parola": "test_parola_hashed",
            "parolaVeche": "test_parola",
            "parolaNoua": "parola_noua",
            "nume": "Gelu",
            "localitateId": "3",
            "telefon": "0123456789"
            }
        """.trimIndent()

        val anuntPart = MockMultipartFile(
            "dto",
            "",
            "application/json",
            json.toByteArray()
        )

        val imaginePart = MockMultipartFile("avatar", "test.jpg", "image/jpeg", "data".toByteArray())

        `when`(utilizatorService.editInfoUtilizator(anyKotlin(), anyKotlin())).thenReturn(dto)

        val requestBuilder = multipart("/api/utilizator/edit")
            .file(anuntPart)
            .file(imaginePart)
            .with { request ->
                request.method = "PUT"
                request
            }
        mockMvc.perform(requestBuilder)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
    }


}