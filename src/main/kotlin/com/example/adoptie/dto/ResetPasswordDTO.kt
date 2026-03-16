package com.example.adoptie.dto

import jakarta.validation.constraints.NotBlank

data class ResetPasswordDTO(
    @field:NotBlank(message = "Token must be valid!")
    val token: String,
    @field:NotBlank(message = "New Password can't be empty!")
    val newPassword: String
)