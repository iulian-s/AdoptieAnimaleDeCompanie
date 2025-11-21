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
    nume = this.nume,
    rol = this.rol,
    localitateId = this.localitate?.id ?: 0,
    telefon = this.telefon,
    avatar = this.avatar,
    dataCreare = this.dataCreare,
    anunturi = this.anunturi
)
/**
 * Citire informatii, parola este exclusa
 */
fun UtilizatorDTO.toEntity(localitate: Localitate): Utilizator = Utilizator(
    id = this.id,
    username = this.username,
    email = this.email,
    nume = this.nume,
    rol = this.rol,
    localitate = localitate,
    telefon = this.telefon,
    avatar = this.avatar,
    dataCreare = this.dataCreare,
    anunturi = this.anunturi
)

/**
 * Creare dto, include parola
 */
fun CreareUtilizatorDTO.toEntity(localitate: Localitate?): Utilizator = Utilizator(
    username = this.username,
    parola = this.parola,
    email = this.email,
    nume = this.nume,
    rol = this.rol,
    localitate = localitate,
    telefon = this.telefon,
    avatar = this.avatar,
    dataCreare = this.dataCreare,
    anunturi = this.anunturi
)

/**
 * Editare dto, include si id si parola
 */
fun EditareUtilizatorDTO.toEntity(localitate: Localitate): Utilizator = Utilizator(
    id = this.id,
    username = this.username,
    parola = this.parola,
    email = this.email,
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
    updatedAt = this.updatedAt,
    locatieId = this.locatie.id,
)

/**
 * Citire si editare anunt
 */
fun AnuntDTO.toEntity(utilizator: Utilizator, locatie: Localitate): Anunt = Anunt(
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
    updatedAt = this.updatedAt,
    locatie = locatie
)

/**
 * Creare anunt
 */
fun CreareAnuntDTO.toEntity(utilizator: Utilizator, locatie: Localitate): Anunt = Anunt(
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
    locatie = locatie
)