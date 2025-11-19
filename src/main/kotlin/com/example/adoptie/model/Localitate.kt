package com.example.adoptie.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "localitati")
data class Localitate(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "nume")
    val nume: String = "",

    @Column(name = "diacritice")
    val diacritice: String = "",

    @Column(name = "judet")
    val judet: String = "",

    @Column(name = "zip")
    val zip: Int = 0,

    @Column(name = "latitudine")
    val latitudine: Double = 0.0,

    @Column(name = "longitudine")
    val longitudine: Double = 0.0
)
