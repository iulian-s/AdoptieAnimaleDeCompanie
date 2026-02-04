package com.example.adoptie.service
import com.example.adoptie.dto.AnuntDTO
import com.example.adoptie.dto.CreareAnuntDTO
import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Gen
import com.example.adoptie.model.Localitate
import com.example.adoptie.model.Rol
import com.example.adoptie.model.Stare
import com.example.adoptie.model.Utilizator
import com.example.adoptie.model.Varsta
import com.example.adoptie.repository.AnunturiRepository
import com.example.adoptie.repository.LocalitateRepository
import com.example.adoptie.repository.UtilizatorRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.web.multipart.MultipartFile
import java.util.Optional

fun <T> anyKotlin(): T {
    return org.mockito.Mockito.any()
}

class AnunturiServiceTest {
    private val anunturiRepository = mock(AnunturiRepository::class.java)
    private val utilizatorRepository = mock(UtilizatorRepository::class.java)
    private val localitateRepository = mock(LocalitateRepository::class.java)
    private val moderareService = mock(ModerareService::class.java)
    private val imagineService = mock(ImagineService::class.java)

    private lateinit var anunturiService: AnunturiService

    @BeforeEach
    fun setup(){
        anunturiService = AnunturiService(
            anunturiRepository,
            utilizatorRepository,
            localitateRepository,
            moderareService,
            imagineService
        )
    }

    @Test
    fun `creareAnunt ar trebui sa seteze starea ACTIV daca imaginile sunt safe`(){
        //simulare user
        val auth = mock(Authentication::class.java)
        val securityContext = mock(SecurityContext::class.java)
        `when`(auth.name).thenReturn("user_test")
        `when`(securityContext.authentication).thenReturn(auth)
        SecurityContextHolder.setContext(securityContext)

        //setup date de intrare
        val dto = CreareAnuntDTO(titlu = "caine", descriere = "pui de caine", gen = Gen.MASCUL, varsta = Varsta.TREI_SASE_LUNI, locatieId = 3)
        val imagini = listOf(mock(MultipartFile::class.java))
        val user = Utilizator(id = 1, username = "user_test", parola = "hash", rol = Rol.USER)
        val locatie = Localitate(id = 3)

        //definire comportament
        `when`(utilizatorRepository.findByUsername("user_test")).thenReturn(user)
        `when`(localitateRepository.findById(3)).thenReturn(Optional.of(locatie))
        `when`(moderareService.suntToateImaginileSafe(imagini)).thenReturn(true)
        `when`(imagineService.saveImage(anyKotlin())).thenReturn("/cale/imagine.jpg")

        //simulare salvare in bd
        `when`(anunturiRepository.save(anyKotlin())).thenAnswer { it.arguments[0] as Anunt}

        //executie
        val rezultat = anunturiService.creareAnunt(dto,imagini)

        //asserts
        assertEquals(Stare.ACTIV, rezultat.stare)
        verify(anunturiRepository).save(anyKotlin())
    }

    @Test
    fun `creareAnunt ar trebui sa seteze starea NEVERIFICAT daca imaginile nu sunt safe`(){
        //simulare user
        val auth = mock(Authentication::class.java)
        val securityContext = mock(SecurityContext::class.java)
        `when`(auth.name).thenReturn("user_test")
        `when`(securityContext.authentication).thenReturn(auth)
        SecurityContextHolder.setContext(securityContext)
        //setup date de intrare
        val dto = CreareAnuntDTO(titlu = "caine", descriere = "pui de caine", gen = Gen.MASCUL, varsta = Varsta.TREI_SASE_LUNI, locatieId = 3)
        val imagini = listOf(mock(MultipartFile::class.java))
        val user = Utilizator(id = 1, username = "user_test", parola = "hash", rol = Rol.USER)
        val locatie = Localitate(id = 3)

        //definire comportament
        `when`(utilizatorRepository.findByUsername("user_test")).thenReturn(user)
        `when`(localitateRepository.findById(3)).thenReturn(Optional.of(locatie))
        `when`(moderareService.suntToateImaginileSafe(imagini)).thenReturn(false)
        `when`(imagineService.saveImage(anyKotlin())).thenReturn("/cale/imagine.jpg")

        //simulare salvare in bd
        `when`(anunturiRepository.save(anyKotlin())).thenAnswer { it.arguments[0] as Anunt}

        //executie
        val rezultat = anunturiService.creareAnunt(dto,imagini)

        //asserts
        assertEquals(Stare.NEVERIFICAT, rezultat.stare)
        verify(anunturiRepository).save(anyKotlin())
    }

    @Test
    fun `getAnuntInRazaFataDeLocatiaSelectata ar trebui sa intoarca doar anunturile din raza data ca parametru`(){
        //setup date de intrare
        val latCentru = 44.4396
        val lonCentru = 26.1012
        val centru = Localitate(id = 1L, nume = "Bucuresti", latitudine = latCentru, longitudine = lonCentru)
        val razaKm = 15.0
        val loc2km = Localitate(id = 2, latitudine = latCentru + 0.018, longitudine = lonCentru) // ~2km
        val loc10km = Localitate(id = 3, latitudine = latCentru + 0.09, longitudine = lonCentru)  // ~10km
        val loc50km = Localitate(id = 4, latitudine = latCentru + 0.45, longitudine = lonCentru)  // ~50km
        val user = Utilizator(id = 1, username = "user_test", parola = "hash", rol = Rol.USER)
        val anunt1 = Anunt(id = 101, titlu = "Aproape", locatie = loc2km, utilizator = user, stare = Stare.ACTIV)
        val anunt2 = Anunt(id = 102, titlu = "La limita", locatie = loc10km, utilizator = user, stare = Stare.ACTIV)
        val anunt3 = Anunt(id = 103, titlu = "Departe", locatie = loc50km, utilizator = user, stare = Stare.ACTIV)
        val anunturiActive = listOf<Anunt>(anunt1, anunt2, anunt3)

        //definire comportament
        `when`(localitateRepository.findById(1L)).thenReturn(Optional.of(centru))
        `when`(anunturiRepository.findByStare(Stare.ACTIV)).thenReturn(anunturiActive)

        //executie
        val rezultat = anunturiService.getAnunturiInRazaFataDeLocatiaSelectata(1L, razaKm)

        //verificare
        assertEquals(2, rezultat.size, "Ar fi trebuit sa fie doar 2 anunturi gasite")
        assertEquals(101L, rezultat[0].id)
        assertEquals(102L, rezultat[1].id)
    }

    @Test
    fun editareAnuntPropriuTest(){
        //simulare user
        val auth = mock(Authentication::class.java)
        val securityContext = mock(SecurityContext::class.java)
        `when`(auth.name).thenReturn("user_test")
        `when`(securityContext.authentication).thenReturn(auth)
        SecurityContextHolder.setContext(securityContext)
        //setup date de intrare
        val user = Utilizator(id = 1, username = "user_test", parola = "hash", rol = Rol.USER)
        val locatie = Localitate(id = 3)
        val imaginiNoi = listOf(mock(MultipartFile::class.java))
        val dto = AnuntDTO(
            utilizatorId = 1,
            titlu = "Titlu dto",
            descriere = "Descriere dto",
            specie = "Caine",
            rasa = "Pechinez",
            gen = Gen.MASCUL,
            varsta = Varsta.NECUNOSCUT,
            stare = Stare.ACTIV,
            locatieId = 3,

        )
        val anunt = Anunt(id = 2,utilizator = user ,locatie = locatie)
        anunt.listaImagini = mutableListOf("/cale/veche.jpg")

        //definire comportament
        `when`(utilizatorRepository.findByUsername("user_test")).thenReturn(user)
        `when`(localitateRepository.findById(3)).thenReturn(Optional.of(locatie))
        `when`(anunturiRepository.findById(2)).thenReturn(Optional.of(anunt))
        `when`(moderareService.suntToateImaginileSafe(imaginiNoi)).thenReturn(true)
        `when`(imagineService.saveImage(anyKotlin())).thenReturn("/cale/noua.jpg")
        //simulare salvare in bd
        `when`(anunturiRepository.save(anyKotlin())).thenAnswer { it.arguments[0] as Anunt}
        //executie
        val rezultat = anunturiService.editareAnuntPropriu(anunt.id, dto, imaginiNoi)
        //asserts
        assertNotNull(rezultat)
        assertEquals(dto.titlu, rezultat.titlu)
        assertEquals(dto.descriere, rezultat.descriere)
        assertEquals(dto.specie, rezultat.specie)
        assertEquals(dto.rasa, rezultat.rasa)
        assertEquals(dto.gen, rezultat.gen)
        assertEquals(dto.varsta, rezultat.varsta)
        assertEquals(dto.utilizatorId, rezultat.utilizator.id)
        assertEquals(dto.locatieId, rezultat.locatie.id)
        assertEquals(dto.stare, rezultat.stare)
        assertEquals(1, rezultat.listaImagini.size)
        assertEquals("/cale/noua.jpg", rezultat.listaImagini[0])
        verify(imagineService).deleteImages(anyKotlin())
        verify(anunturiRepository).save(anyKotlin())
    }

    @Test
    fun editareAnuntPropriuTest_exceptieUserDiferit(){
        //simulam alt user fata de cel ce detine anuntul
        val auth = mock(Authentication::class.java)
        val securityContext = mock(SecurityContext::class.java)
        `when`(auth.name).thenReturn("alt_user")
        `when`(securityContext.authentication).thenReturn(auth)
        SecurityContextHolder.setContext(securityContext)

        //doi useri diferiti
        val altUser = Utilizator(id = 10, username = "alt_user", parola = "hash", rol = Rol.USER)
        val proprietar = Utilizator(id = 1, username = "proprietar", parola = "hash", rol = Rol.USER)

        val anunt = Anunt(id = 50L, utilizator = proprietar, locatie = mock(Localitate::class.java))
        val dto = AnuntDTO(id = 50L, titlu = "Titlu Modificat", utilizatorId = 1L, locatieId = 1L)

        //mockuri
        `when`(utilizatorRepository.findByUsername("alt_user")).thenReturn(altUser)
        `when`(anunturiRepository.findById(anunt.id)).thenReturn(Optional.of(anunt))

        //executie si assert
        val exceptie = assertThrows(IllegalAccessException::class.java) {
            anunturiService.editareAnuntPropriu(anunt.id, dto, null)
        }
        assertEquals("Anuntul ales nu apartine utilizatorului logat!", exceptie.message)
        verify(anunturiRepository, never()).save(anyKotlin())
    }

    @Test
    fun vizualizareAnunturiPropriiTest(){
        //simulare user
        val auth = mock(Authentication::class.java)
        val securityContext = mock(SecurityContext::class.java)
        `when`(auth.name).thenReturn("user_test")
        `when`(securityContext.authentication).thenReturn(auth)
        SecurityContextHolder.setContext(securityContext)

        //obiect user
        val user = Utilizator(id = 10L, username = "user_test", parola = "hash", rol = Rol.USER)
        val altUser = Utilizator(id = 11L, username = "altUser_test", parola = "hash", rol = Rol.USER)
        val anunt1 = Anunt(id = 1L, utilizator = user, locatie = mock(Localitate::class.java))
        val anunt2 = Anunt(id = 2L, utilizator = user, locatie = mock(Localitate::class.java))
        val anunt3 = Anunt(id = 3L, utilizator = altUser, locatie = mock(Localitate::class.java))
        //comportament
        `when`(utilizatorRepository.findByUsername("user_test")).thenReturn(user)
        `when`(anunturiRepository.findByUtilizator_Id(user.id)).thenReturn(listOf(anunt1, anunt2))
        //executie
        val rezultat = anunturiService.anunturiUtilizator()
        //asserts
        assertEquals(2, rezultat.size)
        assertEquals(1L, rezultat[0].id)
        assertEquals(2L, rezultat[1].id)
        val contineAnuntStrain = rezultat.any { it.id == 3L }
        assertFalse(contineAnuntStrain, "Rezultatul nu ar trebui sa contina anunturi ale altor utilizatori")
        verify(anunturiRepository).findByUtilizator_Id(10L)
        verify(anunturiRepository, never()).findByUtilizator_Id(11L)
    }



}