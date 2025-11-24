package com.example.adoptie.dto

import com.example.adoptie.model.Rol
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

/**
 * contine si id si parola pentru editare
 */
data class EditareUtilizatorDTO(
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
    var avatar: String ="",
    val dataCreare: LocalDateTime = LocalDateTime.now(),
    var anuntIds: MutableList<Long> = mutableListOf()
)
