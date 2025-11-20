package com.example.adoptie.dto

import com.example.adoptie.model.Gen
import com.example.adoptie.model.Stare
import com.example.adoptie.model.Varsta
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class AnuntDTO(
    val id: Long = 0,

    @field:NotBlank(message = "Campul trebuie completat!")
    var titlu: String = "",

    @field:NotBlank(message = "Campul trebuie completat!")
    var descriere: String = "",

    @field:NotBlank(message = "Campul trebuie completat!")
    var specie: String = "",

    @field:NotBlank(message = "Campul trebuie completat!")
    var rasa: String = "",
    var gen: Gen = Gen.MASCUL,
    var varsta: Varsta = Varsta.NECUNOSCUT,
    var varstaMin: Int? = varsta.minLuni,
    var varstaMax: Int? = varsta.maxLuni,
    val utilizatorId: Long = 0,
    var listaImagini: MutableList<String> = mutableListOf(),
    var stare: Stare = Stare.NEVERIFICAT,
    val createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var locatieId: Long = 0

    )
