package com.example.adoptie.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

/**
 * Clasa entitate unde se inregistreaza actiunile utilizatorilor intr-un tabel tip log in baza de date
 */
@Entity
@Table(name = "logActiune")
data class LogActiune(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val username: String,
    val endpoint: String,
    val metoda: String,
    val status: Int,

    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    val timestamp: LocalDateTime = LocalDateTime.now()
)
