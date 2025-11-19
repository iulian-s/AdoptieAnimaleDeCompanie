package com.example.adoptie.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Table(name = "anunturi")
@Entity
data class Anunt(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "titlu")
    val titlu: String = "",

    @Column(name = "descriere")
    val descriere: String = "",

    @Column(name = "specie")
    val specie: String = "",

    @Column(name = "rasa")
    val rasa: String = "",

    @Column(name = "gen")
    @Enumerated(EnumType.STRING)
    val gen: Gen = Gen.MASCUL,

    @Column(name = "varsta")
    val varsta: Varsta = Varsta.NECUNOSCUT,

    @Column(name = "varsta_min")
    val varstaMin: Int? = varsta.minLuni,

    @Column(name = "varsta_max")
    val varstaMax: Int? = varsta.maxLuni,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilizator")
    val utilizator: Utilizator,

    @Column(name = "lista_imagini")
    val listaImagini: MutableList<String> = mutableListOf(),

    @Column(name = "stare")
    @Enumerated(EnumType.STRING)
    val stare: Stare = Stare.NEVERIFICAT,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
)
