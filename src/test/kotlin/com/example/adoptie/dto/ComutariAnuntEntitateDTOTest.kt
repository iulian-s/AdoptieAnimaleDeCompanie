package com.example.adoptie.dto

import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Gen
import com.example.adoptie.model.Localitate
import com.example.adoptie.model.Rol
import com.example.adoptie.model.Stare
import com.example.adoptie.model.Utilizator
import com.example.adoptie.model.Varsta
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.time.LocalDateTime
import kotlin.test.assertTrue

class ComutariAnuntEntitateDTOTest {
    @Test
    fun comutareEntitateLaDTO() {
        val localitate = Localitate(id = 3)
        val utilizator = Utilizator(id = 2)
        
        val anuntEntity = Anunt(
            id = 4,
            titlu = "Nicusor",
            descriere = "Catel dat spre adoptie",
            specie = "Caine",
            rasa = "Labrador",
            gen = Gen.MASCUL,
            varsta = Varsta.NECUNOSCUT,
            utilizator = utilizator,
            listaImagini = mutableListOf<String>("/img1.png", "/img2.png"),
            stare = Stare.NEVERIFICAT,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            locatie = localitate
        )
        val dto = anuntEntity.toDTO()

        assertEquals(anuntEntity.id, dto.id)
        assertEquals(anuntEntity.titlu, dto.titlu)
        assertEquals(anuntEntity.descriere, dto.descriere)
        assertEquals(anuntEntity.specie, dto.specie)
        assertEquals(anuntEntity.stare, dto.stare)
        assertEquals(anuntEntity.locatie.id, dto.locatieId)
        assertEquals(anuntEntity.utilizator.id, dto.utilizatorId)
        assertEquals(anuntEntity.listaImagini.size, dto.listaImagini.size)
        assertEquals(anuntEntity.listaImagini, dto.listaImagini)
        assertEquals(anuntEntity.createdAt, dto.createdAt)
        assertEquals(anuntEntity.updatedAt, dto.updatedAt)
    }

    @Test
    fun comutareEntitateLaDTO_campuriNule() {
        val localitate = Localitate(id = 3)
        val utilizator = Utilizator(id = 2)

        val anuntEntity = Anunt(
            id = 4,
            titlu = "Nicusor",
            descriere = "Catel dat spre adoptie",
            specie = "Caine",
            rasa = "Labrador",
            gen = Gen.MASCUL,
            varsta = Varsta.NECUNOSCUT,
            utilizator = utilizator,
            listaImagini = mutableListOf<String>("/img1.png", "/img2.png"),
            stare = Stare.NEVERIFICAT,
            locatie = localitate,
            createdAt = null,
            updatedAt = null,
            varstaMax = null,
            varstaMin = null,
        )
        val dto = anuntEntity.toDTO()
        assertNotNull(dto)
        assertEquals(anuntEntity.createdAt, dto.createdAt)
        assertNull(anuntEntity.createdAt)
        assertNull(dto.createdAt)
        assertEquals(anuntEntity.updatedAt, dto.updatedAt)
        assertNull(anuntEntity.updatedAt)
        assertNull(dto.updatedAt)
        assertNull(dto.varstaMax)
        assertNull(dto.varstaMin)
        assertEquals(anuntEntity.varstaMin, dto.varstaMin)
        assertEquals(anuntEntity.varstaMax, dto.varstaMax)
    }

    @Test
    fun comutareEntitateLaDTO_listaImaginiGoala() {
        val localitate = Localitate(id = 3)
        val utilizator = Utilizator(id = 2)

        val anuntEntity = Anunt(
            id = 4,
            titlu = "Nicusor",
            descriere = "Catel dat spre adoptie",
            specie = "Caine",
            rasa = "Labrador",
            gen = Gen.MASCUL,
            varsta = Varsta.NECUNOSCUT,
            utilizator = utilizator,
            listaImagini = emptyList<String>().toMutableList(),
            stare = Stare.NEVERIFICAT,
            locatie = localitate
        )
        val dto = anuntEntity.toDTO()
        assertTrue(anuntEntity.listaImagini.isEmpty())
        assertTrue(dto.listaImagini.isEmpty())
        assertEquals(anuntEntity.listaImagini.size, dto.listaImagini.size)
        assertEquals(anuntEntity.listaImagini, dto.listaImagini)
    }

    @Test
    fun valoriImplicitePeCampuriNecompletate() {
        val localitate = Localitate(id = 1L, nume = "Bucuresti", judet = "Bucuresti", latitudine = 0.0, longitudine = 0.0)
        val utilizator = Utilizator(id = 1L, username = "test", parola = "pass", rol = Rol.USER)

        val anuntDefault = Anunt(
            utilizator = utilizator,
            locatie = localitate
            // Restul campurilor vor lua valorile default din definitia clasei
        )
        val dto = anuntDefault.toDTO()

        assertEquals("", dto.titlu) // Verificam ca default-ul de "" s-a dus in DTO
        assertEquals(Gen.MASCUL, dto.gen)
        assertEquals(Stare.NEVERIFICAT, dto.stare)
        assertTrue(dto.listaImagini.isEmpty())
    }


    //invers dto - entitate
    @Test
    fun comutareDTOLaEntitate() {
        val localitate = Localitate(id = 3)
        val utilizator = Utilizator(id = 2)

        val dto = AnuntDTO(
            id = 4,
            titlu = "Nicusor",
            descriere = "Catel dat spre adoptie",
            specie = "Caine",
            rasa = "Labrador",
            gen = Gen.MASCUL,
            varsta = Varsta.NECUNOSCUT,
            utilizatorId = utilizator.id,
            listaImagini = mutableListOf<String>("/img1.png", "/img2.png"),
            stare = Stare.NEVERIFICAT,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            locatieId = localitate.id
        )
        val anuntEntity = dto.toEntity(utilizator, localitate)

        assertEquals(dto.id, anuntEntity.id)
        assertEquals(dto.titlu, anuntEntity.titlu)
        assertEquals(dto.descriere, anuntEntity.descriere)
        assertEquals(dto.specie, anuntEntity.specie)
        assertEquals(dto.stare, anuntEntity.stare)
        assertEquals(dto.locatieId, anuntEntity.locatie.id)
        assertEquals(dto.utilizatorId, anuntEntity.utilizator.id)
        assertEquals(dto.listaImagini.size, anuntEntity.listaImagini.size)
        assertEquals(dto.listaImagini, anuntEntity.listaImagini)
        assertEquals(dto.createdAt, anuntEntity.createdAt)
        assertEquals(dto.updatedAt, anuntEntity.updatedAt)
    }


}