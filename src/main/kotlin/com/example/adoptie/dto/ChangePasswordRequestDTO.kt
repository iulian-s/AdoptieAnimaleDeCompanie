package com.example.adoptie.dto

import jakarta.validation.constraints.NotBlank

data class ChangePasswordRequestDTO(
    var oldPassword: String,

    var newPassword: String
)