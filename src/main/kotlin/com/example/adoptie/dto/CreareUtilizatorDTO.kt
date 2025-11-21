package com.example.adoptie.dto

import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Rol
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

/**
 * Nu contine id la creare, baza de date se ocupa de gestiunea id urilor
 */
data class CreareUtilizatorDTO(
    @field:NotBlank(message = "Campul trebuie completat!")
    val username: String = "",

    @field:NotBlank(message = "Campul trebuie completat!")
    var parola: String = "",

    @field:Email(message = "Email invalid!")
    val email: String = "",

    val rol: Rol,
    var nume: String,
    var localitateId: Long = 0,
    var telefon: String = "",
    var avatar: String ="",
    val dataCreare: LocalDateTime,
    var anunturi: MutableList<Anunt> = mutableListOf()
)
