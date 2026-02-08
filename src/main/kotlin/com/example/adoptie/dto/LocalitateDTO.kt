package com.example.adoptie.dto

/**
 * Reprezentarea campurilor din tabela localitati din baza de date, utilizata in gestiunea locatiei unui utilizator prin controllere
 */
data class LocalitateDTO(
    var id: Long = 0,
    val nume: String = "",
    val diacritice: String = "",
    val judet: String = "",
    val auto: String = "",
    val zip: Int = 0,
    val populatie: Int = 0,
    val lat: Double = 0.0,
    val lng: Double = 0.0
)


