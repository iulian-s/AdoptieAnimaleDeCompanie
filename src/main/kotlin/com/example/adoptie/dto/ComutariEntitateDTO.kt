package com.example.adoptie.dto

import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Localitate
import com.example.adoptie.model.Utilizator
import com.example.adoptie.repository.UtilizatorRepository

/**
 * Functii de comutare intre entitate si DTO
 */
fun Localitate.toDTO(): LocalitateDTO = LocalitateDTO(
    id = this.id,
    nume = this.nume,
    diacritice = this.diacritice,
    judet = this.judet,
    zip = this.zip,
    lat = this.latitudine,
    lng = this.longitudine,
)

fun LocalitateDTO.toEntity(): Localitate = Localitate(
    id = this.id,
    nume = this.nume,
    diacritice = this.diacritice,
    judet = this.judet,
    zip = this.zip,
    latitudine = lat,
    longitudine = lng
)

fun Utilizator.toDTO(): UtilizatorDTO = UtilizatorDTO(
    id = this.id,
    username = this.username,
    email = this.email,
    parola = this.parola,
    nume = this.nume,
    rol = this.rol,
    localitateId = this.localitate?.id ?: 0,
    telefon = this.telefon,
    avatar = this.avatar,
    dataCreare = this.dataCreare,
    anunturi = this.anunturi
)

fun UtilizatorDTO.toEntity(localitate: Localitate): Utilizator = Utilizator(
    id = this.id,
    username = this.username,
    email = this.email,
    parola = this.parola,
    nume = this.nume,
    rol = this.rol,
    localitate = localitate,
    telefon = this.telefon,
    avatar = this.avatar,
    dataCreare = this.dataCreare,
    anunturi = this.anunturi
)

fun Anunt.toDTO(): AnuntDTO = AnuntDTO(
    id = this.id,
    titlu = this.titlu,
    descriere = this.descriere,
    specie = this.specie,
    rasa = this.rasa,
    gen = this.gen,
    varsta = this.varsta,
    varstaMin = this.varstaMin,
    varstaMax = this.varstaMax,
    utilizatorId = utilizator.id,
    listaImagini = this.listaImagini,
    stare = this.stare,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun AnuntDTO.toEntity(utilizator: Utilizator): Anunt = Anunt(
    id = this.id,
    titlu = this.titlu,
    descriere = this.descriere,
    specie = this.specie,
    rasa = this.rasa,
    gen = this.gen,
    varsta = this.varsta,
    varstaMin = this.varstaMin,
    varstaMax = this.varstaMax,
    utilizator = utilizator,
    listaImagini = this.listaImagini,
    stare = this.stare,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)