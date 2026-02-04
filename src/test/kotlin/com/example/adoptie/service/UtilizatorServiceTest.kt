package com.example.adoptie.service

import com.example.adoptie.dto.CreareUtilizatorDTO
import com.example.adoptie.dto.EditareUtilizatorDTO
import com.example.adoptie.dto.LocalitateDTO
import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Localitate
import com.example.adoptie.model.Rol
import com.example.adoptie.model.Utilizator
import com.example.adoptie.repository.AnunturiRepository
import com.example.adoptie.repository.LocalitateRepository
import com.example.adoptie.repository.UtilizatorRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.multipart.MultipartFile

class UtilizatorServiceTest {
    private val anunturiRepository = mock(AnunturiRepository::class.java)
    private val utilizatorRepository = mock(UtilizatorRepository::class.java)
    private val localitateService = mock(LocalitateService::class.java)
    private val imagineService = mock(ImagineService::class.java)
    private val passwordEncoder = mock(PasswordEncoder::class.java)
    private lateinit var utilizatorService: UtilizatorService

    @BeforeEach
    fun setup() {
        utilizatorService = UtilizatorService(
            utilizatorRepository = utilizatorRepository,
            passwordEncoder = passwordEncoder,
            anunturiRepository = anunturiRepository,
            localitateService = localitateService,
            imagineService = imagineService,
        )
    }

    @Test
    fun inregistrareUtilizatorTest() {
        //date de intrare
        val dto = CreareUtilizatorDTO(
            username = "user_test",
            parola = "1234",
            email = "abc@gmail.com",
            rol = Rol.USER,
            avatar = "/storage/v1/object/public/uploads/avatar.png",
            judet = "Alba",
            localitate = "Alba Iulia"
        )
        val localitate = Localitate(id = 1L, judet = "Alba", nume = "Alba Iulia")
        val parolaEncodata = "parola_hash_1234"
        //comportament
        `when`(utilizatorRepository.findByUsername(dto.username)).thenReturn(null)
        `when`(passwordEncoder.encode(dto.parola)).thenReturn(parolaEncodata)
        `when`(localitateService.getLocalitateByJudetAndNume(dto.judet, dto.localitate)).thenReturn(localitate)
        `when`(utilizatorRepository.save(anyKotlin())).thenAnswer { it.arguments[0] as Utilizator }

        //executie
        val rezultat = utilizatorService.inregistrare(dto)
        //asserts
        assertNotNull(rezultat)
        assertEquals(localitate, rezultat.localitate)
        assertEquals(parolaEncodata, rezultat.parola)
        assertEquals(dto.email, rezultat.email)
        assertEquals(dto.rol, rezultat.rol)
        assertEquals(dto.avatar, rezultat.avatar)
        assertEquals(dto.judet, dto.judet)
        assertEquals(dto.username, rezultat.username)
        assertEquals(dto.nume, rezultat.nume)

        verify(utilizatorRepository, times(1)).findByUsername(dto.username)
        verify(utilizatorRepository, times(1)).save(anyKotlin())
    }

    @Test
    fun infoUtilizatorTest() {
        //simulare user
        val auth = mock(Authentication::class.java)
        val securityContext = mock(SecurityContext::class.java)
        `when`(auth.name).thenReturn("user_test")
        `when`(securityContext.authentication).thenReturn(auth)
        SecurityContextHolder.setContext(securityContext)
        //setup date de intrare
        val user = Utilizator(id = 1L, username = "user_test")
        val user2 = Utilizator(id = 2L, username = "user_test2")
        val localitate = Localitate(id = 1L, judet = "Alba", nume = "Alba Iulia")
        val anunt1 = Anunt(id = 10L,utilizator = user, locatie = localitate)
        val anunt2 = Anunt(id = 11L,utilizator = user2, locatie = localitate)
        val anunturi = listOf<Anunt>(anunt1, anunt2)
        //comportament
        `when`(utilizatorRepository.findByUsername("user_test")).thenReturn(user)
        `when`(anunturiRepository.findByUtilizator_Id(user.id)).thenReturn(listOf(anunturi[0]))
        //executie
        val rezultat = utilizatorService.infoUtilizator()
        //asserts
        assertNotNull(rezultat)
        assertEquals(1, rezultat.anunturi?.size)
        assertEquals(10L, rezultat.anunturi?.get(0)?.id)
        assertEquals(user.id, rezultat.anunturi?.get(0)?.utilizator?.id)
        assertEquals(user.username, rezultat.username)
        verify(anunturiRepository).findByUtilizator_Id(user.id)
    }

    @Test
    fun editInfoUtilizatorTest() {
        //simulare user
        val auth = mock(Authentication::class.java)
        val securityContext = mock(SecurityContext::class.java)
        `when`(auth.name).thenReturn("user_test")
        `when`(securityContext.authentication).thenReturn(auth)
        SecurityContextHolder.setContext(securityContext)
        //setup date de intrare
        val user = Utilizator(id = 1L, username = "user_test", parola = "parola_hash_1234")
        val parolaEncodata = "parola_hash_1234"
        val imagine = mock(MultipartFile::class.java)
        val dto = EditareUtilizatorDTO(
            username = "user_test",
            parola = "1234",
            rol = Rol.USER,
            parolaNoua = "2345",
            parolaVeche = "1234"

        )
        //comportament
        `when`(utilizatorRepository.findByUsername("user_test")).thenReturn(user)
        `when`(passwordEncoder.matches(dto.parolaVeche, parolaEncodata)).thenReturn(true)
        `when`(passwordEncoder.encode(dto.parolaNoua)).thenReturn(parolaEncodata)
        `when`(imagineService.saveImage(anyKotlin())).thenReturn("/cale/imagine.jpg")

        `when`(utilizatorRepository.save(anyKotlin())).thenAnswer { it.arguments[0] as Utilizator }

        //executie
        val rezultat = utilizatorService.editInfoUtilizator(dto, imagine)
        //asserts
        assertNotNull(rezultat)
        assertEquals(parolaEncodata, rezultat.parola)
        assertEquals(dto.username, rezultat.username)
        assertEquals(dto.rol, rezultat.rol)
        assertEquals("/cale/imagine.jpg", rezultat.avatar)
        assertEquals(dto.telefon, rezultat.telefon)

        verify(utilizatorRepository).save(anyKotlin())
        verify(utilizatorRepository, times(1)).findByUsername(dto.username)
    }

    @Test
    fun stergereContUtilizatorTest() {
        //simulare user
        val auth = mock(Authentication::class.java)
        val securityContext = mock(SecurityContext::class.java)
        `when`(auth.name).thenReturn("user_test")
        `when`(securityContext.authentication).thenReturn(auth)
        SecurityContextHolder.setContext(securityContext)
        //setup date de intrare
        val user = Utilizator(id = 1L, username = "user_test", parola = "parola_hash_1234")
        val anunt1 = Anunt(id = 10L,utilizator = user, locatie = mock(Localitate::class.java))
        val anunt2 = Anunt(id = 11L,utilizator = user, locatie = mock(Localitate::class.java))
        val anunturi = listOf<Anunt>(anunt1, anunt2)
        val parolaTrimisa = "1234"
        //comportament
        `when`(utilizatorRepository.findByUsername("user_test")).thenReturn(user)
        `when`(anunturiRepository.findByUtilizator_Id(user.id)).thenReturn(anunturi)
//        `when`(imagineService.deleteImages(anyKotlin()))
//        `when`(imagineService.deleteImage(anyKotlin()))
        `when`(passwordEncoder.matches(parolaTrimisa, user.parola)).thenReturn(true)
        //`when`(utilizatorRepository.delete(anyKotlin()))
        //executie
        val rezultat = utilizatorService.stergeContUtilizator(parolaTrimisa)
        assertNotNull(rezultat)
        assertTrue(rezultat)
        verify(utilizatorRepository).delete(anyKotlin())
        verify(imagineService, times(2)).deleteImages(anyKotlin())
        verify(imagineService, times(1)).deleteImage(user.avatar)
    }

}