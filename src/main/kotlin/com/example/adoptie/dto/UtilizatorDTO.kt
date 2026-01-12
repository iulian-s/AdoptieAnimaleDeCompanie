package com.example.adoptie.dto

import com.example.adoptie.model.Rol
import jakarta.validation.constraints.*
import java.time.LocalDateTime

/**
 * Nu contine parola, se foloseste la citire
 */
data class UtilizatorDTO(
    val id: Long = 0,

    @field:NotBlank(message = "Campul trebuie completat!")
    val username: String = "",

   @field:NotBlank(message = "Campul trebuie completat!")
    var parola: String = "",

    @field:Email(message = "Email invalid!")
    val email: String = "",

    val rol: Rol = Rol.USER,
    var nume: String = "",
    var localitateId: Long = 0,
    var telefon: String = "",
    var avatar: String? = null,
    val dataCreare: LocalDateTime = LocalDateTime.now(),

    var anuntIds: MutableList<Long> = mutableListOf()
)