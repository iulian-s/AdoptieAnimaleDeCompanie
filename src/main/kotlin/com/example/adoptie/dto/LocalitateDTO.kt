package com.example.adoptie.dto

/**
 * Reprezentarea campurilor din tabela localitati din baza de date, utilizata in gestiunea locatiei unui utilizator prin controllere
 */
data class LocalitateDTO(
    var id: Long,
    val nume: String,
    val diacritice: String,
    val judet: String,
    val auto: String = "",
    val zip: Int,
    val populatie: Int = 0,
    val lat: Double,
    val lng: Double
)


