package com.example.adoptie.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ForgotPasswordRequestDTO(
    var email: String
)