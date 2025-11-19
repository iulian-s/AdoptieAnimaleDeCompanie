package com.example.adoptie.dto

data class LocalitateJson(
    val id: Long,
    val nume: String,
    val diacritice: String,
    val judet: String,
    val auto: String,
    val zip: Int,
    val populatie: Int,
    val lat: Double,
    val lng: Double
)


