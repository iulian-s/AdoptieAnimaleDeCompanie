package com.example.adoptie.dto

import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Rol
import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class UtilizatorDTO(
    val id: Long = 0,

    @field:NotBlank("Campul trebuie completat!")
    val username: String = "",

    @field:NotBlank("Campul trebuie completat!")
    var parola: String = "",

    @field:Email("Email invalid!")
    val email: String = "",

    val rol: Rol,
    var nume: String,
    var localitateId: Long = 0,
    var telefon: String = "",
    var avatar: String ="",
    val dataCreare: LocalDateTime,
    var anunturi: MutableList<Anunt> = mutableListOf()
)