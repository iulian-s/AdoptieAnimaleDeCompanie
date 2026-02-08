package com.example.adoptie.dto

import com.example.adoptie.model.Gen
import com.example.adoptie.model.Stare
import com.example.adoptie.model.Varsta
import jakarta.validation.constraints.NotBlank

data class CreareAnuntDTO(
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
    //nu este necesara variabila pentru ca in service am parametru separat pentru imagini, iar lista de imagini din entitate se populeaza din acesta, service ul primeste dto, il converteste, ii aplica instructiuni si l salveaza ca entitate
    //nu e folosit la niciun request get, asa ca nu e necesara salvarea listei de imagini in acest dto
    //var listaImagini: MutableList<String> = mutableListOf(),
    var stare: Stare = Stare.NEVERIFICAT,
    var locatieId: Long = 0
)
