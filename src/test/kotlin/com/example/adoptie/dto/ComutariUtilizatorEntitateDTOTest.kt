package com.example.adoptie.dto

import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Localitate
import com.example.adoptie.model.Utilizator
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class ComutariUtilizatorEntitateDTOTest {
    @Test
    fun comutareEntitateDTOTest(){
        val localitate = Localitate()
        val anunturi = listOf<Anunt>().toMutableList()

        val utilizator = Utilizator(
            localitate = localitate,
            anunturi = anunturi,
        )
        val dto = utilizator.toDTO()

        assertEquals(utilizator.id, dto.id)
        assertEquals(utilizator.username, dto.username)
        assertEquals(utilizator.email, dto.email)
        assertEquals(utilizator.rol, dto.rol)
        assertEquals(utilizator.avatar, dto.avatar)
        assertEquals(utilizator.nume, dto.nume)
        assertEquals(utilizator.telefon, dto.telefon)
        assertEquals(utilizator.anunturi?.map{it.id}, dto.anuntIds)
        assertEquals(utilizator.dataCreare, dto.dataCreare)
        assertEquals(utilizator.localitate?.id, dto.localitateId )
    }


}