package com.example.adoptie.controller

import com.example.adoptie.config.LogActiuneInterceptor
import com.example.adoptie.config.LogConfig
import com.example.adoptie.dto.AnuntDTO
import com.example.adoptie.dto.toEntity
import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Gen
import com.example.adoptie.model.Localitate
import com.example.adoptie.model.Stare
import com.example.adoptie.model.Utilizator
import com.example.adoptie.model.Varsta
import com.example.adoptie.service.AnunturiService
import com.example.adoptie.service.DetaliiUtilizatorService
import com.example.adoptie.service.JwtService
import com.example.adoptie.service.RateLimitService
import com.example.adoptie.service.anyKotlin
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.bucket4j.Bucket
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@WebMvcTest(
    controllers = [AnunturiController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [LogConfig::class]
        )
    ]
)
@AutoConfigureMockMvc(addFilters = false)
class AnunturiControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var anunturiService: AnunturiService

    @MockitoBean
    private lateinit var jwtService: JwtService

    @MockitoBean
    private lateinit var detaliiUtilizatorService: DetaliiUtilizatorService

    @MockitoBean
    private lateinit var logActiuneInterceptor: LogActiuneInterceptor
    @MockitoBean
    private lateinit var rateLimitService: RateLimitService

    @Test
    fun `returnare lista cu anunturi active cu status 200`() {
        val locatie = Localitate(id = 3L)
        val utilizator = Utilizator(id = 1L, username = "test_user", localitate = locatie)
        val anuntDTO = AnuntDTO(
            id = 1L,
            titlu = "Titlu",
            descriere = "Descriere",
            specie = "Caine",
            rasa = "Pechinez",
            gen = Gen.FEMELA,
            varsta = Varsta.TREI_SASE_LUNI,
            stare = Stare.ACTIV,
            locatieId = locatie.id,
            utilizatorId = utilizator.id
        )
        val anunt = anuntDTO.toEntity(utilizator, locatie)



        `when`(anunturiService.vizualizareAnunturiActive()).thenReturn(listOf(anunt))

        //executie
        mockMvc.perform(get("/api/anunturi/active"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].stare").value("ACTIV"))

    }

    @Test
    fun `ar trebui sa returneze 200 cand campurile sunt completate`() {
        // Populate SecurityContext manually
        val authentication = UsernamePasswordAuthenticationToken(
            "test_user",
            "password",
            listOf(SimpleGrantedAuthority("ROLE_USER"))
        )
        SecurityContextHolder.getContext().authentication = authentication

        val anuntIncompletJson = """
        {
            "titlu": "Catel", 
            "descriere": "Descrierea catelului",
            "specie": "Caine",
            "rasa": "Metis",
            "gen": "MASCUL",
            "varsta": "${Varsta.TREI_SASE_LUNI}",
            "stare": "${Stare.ACTIV}",
            "utilizatorId": 3,
            "locatieId": 1
        }
    """.trimIndent()

        val anuntPart = MockMultipartFile(
            "anunt",
            "",
            "application/json",
            anuntIncompletJson.toByteArray()
        )

        val imaginePart = MockMultipartFile(
            "imagini",
            "test.jpg",
            "image/jpeg",
            "data".toByteArray()
        )

        val bucket = mock(Bucket::class.java)
        `when`(bucket.tryConsume(1)).thenReturn(true)
        `when`(rateLimitService.resolveBucket(anyKotlin())).thenReturn(bucket)

        val anuntMock = AnuntDTO(
            id = 100L,
            titlu = "Catel",
            utilizatorId = 2,
            locatieId = 3
        ).toEntity(mock(Utilizator::class.java), mock(Localitate::class.java))
        `when`(anunturiService.creareAnunt(anyKotlin(), anyKotlin())).thenReturn(anuntMock)

        mockMvc.perform(
            multipart("/api/anunturi")
                .file(anuntPart)
                .file(imaginePart)
        )
            .andDo(print())
            .andExpect(status().isOk)
    }



    @Test
    fun `ar trebui sa returneze 400 cand camp not blank este gol`() {
        val anuntIncompletJson = """
        {
            "titlu": "", 
            "descriere": "",
            "specie": "",
            "rasa": "",
            "locatieId": 1
        }
    """.trimIndent()

        val anuntPart = MockMultipartFile(
            "anunt",
            "",
            "application/json", // CRITIC: Trebuie sa fie application/json
            anuntIncompletJson.toByteArray()
        )

        val imaginePart = MockMultipartFile("imagini", "test.jpg", "image/jpeg", "data".toByteArray())
        val anuntMock = AnuntDTO(
            id = 100L,
            titlu = "Catel",
            utilizatorId = 2,
            locatieId = 3
        ).toEntity(mock(Utilizator::class.java), mock(Localitate::class.java))
        `when`(anunturiService.creareAnunt(anyKotlin(), anyKotlin())).thenReturn(anuntMock)
        mockMvc.perform(
            multipart("/api/anunturi")
                .file(anuntPart)
                .file(imaginePart)
        )
            .andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `editareAnunt de catre admin ar trebui sa returneze 200 OK`() {
        val idAnunt = 1L

        // dto ul de trimis
        val editDto = AnuntDTO(
            id = idAnunt,
            titlu = "Titlu Modificat de Admin",
            descriere = "Descriere valida",
            specie = "Caine",
            rasa = "Labrador",
            stare = Stare.ACTIV,
            locatieId = 1L,
            utilizatorId = 1L
        )

        // 2. entitatea care pacaleste service ul
        val anuntSalvat = Anunt(
            id = idAnunt,
            titlu = "Titlu Modificat de Admin",
            stare = Stare.ACTIV,
            utilizator = mock(Utilizator::class.java),
            locatie = mock(Localitate::class.java)
        )

        // 3. Mocking Service
        `when`(anunturiService.editareAnunt(eq(idAnunt), anyKotlin())).thenReturn(anuntSalvat)

        // 4. Executie
        mockMvc.perform(
            put("/api/anunturi/{id}", idAnunt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(editDto)) // Convertim DTO în JSON
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(idAnunt))
            .andExpect(jsonPath("$.stare").value("ACTIV"))
    }

    @Test
    @WithMockUser(username = "user_test", roles = ["USER"])
    fun `editareAnuntPropriu ar trebui sa returneze 200 OK`() {
        val idAnunt = 100L

        // 1. Pregătim JSON-ul pentru partea numită "dto"
        val anuntDtoJson = """
        {
            "id": $idAnunt,
            "titlu": "Titlu Nou",
            "descriere": "Descriere actualizata",
            "specie": "Pisica",
            "rasa": "Europeana",
            "gen": "FEMELA",
            "varsta": "UNU_TREI_ANI",
            "locatieId": 1,
            "utilizatorId": 1
        }
    """.trimIndent()

        val dtoPart = MockMultipartFile(
            "dto",
            "",
            "application/json",
            anuntDtoJson.toByteArray()
        )

        // 2. Pregătim o imagine nouă (opțională)
        val imaginePart = MockMultipartFile(
            "imagini",
            "noua_imagine.jpg",
            "image/jpeg",
            "content".toByteArray()
        )

        // 3. Mocking Service
        val anuntUpdate = Anunt(
            id = idAnunt,
            titlu = "Titlu Nou",
            utilizator = mock(Utilizator::class.java),
            locatie = mock(Localitate::class.java)
        )
        `when`(anunturiService.editareAnuntPropriu(eq(idAnunt), anyKotlin(), anyKotlin())).thenReturn(anuntUpdate)

        // 4. Execuție (Folosim multipart cu PUT override)
        mockMvc.perform(
            multipart("/api/anunturi/eu/{id}", idAnunt)
                .file(dtoPart)
                .file(imaginePart)
                .with { request -> request.method = "PUT"; request } // Important: Schimbăm POST în PUT
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.titlu").value("Titlu Nou"))
    }

}