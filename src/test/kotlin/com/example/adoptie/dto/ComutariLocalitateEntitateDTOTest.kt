package com.example.adoptie.dto

import com.example.adoptie.model.Localitate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ComutariLocalitateEntitateDTOTest {
    @Test
    fun mapareLocalitateEntityLaDTO() {
        val localitate = Localitate(
            id = 3L,
            nume = "Macin",
            diacritice = "Macin",
            judet = "Tulcea",
            zip = 1234,
            latitudine = 5678.0,
            longitudine = 5678.0
        )
        val localitateDto = localitate.toDTO()
        assertNotNull(localitateDto)
        assertEquals(localitate.nume, localitateDto.nume)
        assertEquals(localitate.id, localitateDto.id)
        assertEquals(localitate.diacritice, localitateDto.diacritice)
        assertEquals(localitate.judet, localitateDto.judet)
        assertEquals(localitate.zip, localitateDto.zip)
        assertEquals(localitate.latitudine, localitateDto.lat)
        assertEquals(localitate.longitudine, localitateDto.lng)
    }

    @Test
    fun mapareLocalitateDtoLaEntity(){
        val dto = LocalitateDTO(
            id = 3L,
            nume = "Macin",
            diacritice = "Macin",
            judet = "Tulcea",
            zip = 1234,
            lat = 5678.0,
            lng = 5678.0
        )

        val localitate = dto.toEntity()
        assertNotNull(localitate)
        assertEquals(dto.id, localitate.id)
        assertEquals(dto.nume, localitate.nume)
        assertEquals(dto.diacritice, localitate.diacritice)
        assertEquals(dto.judet, localitate.judet)
        assertEquals(dto.zip, localitate.zip)
        assertEquals(dto.lat, localitate.latitudine)
        assertEquals(dto.lng, localitate.longitudine)
    }

}