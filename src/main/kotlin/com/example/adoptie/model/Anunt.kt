package com.example.adoptie.model

import com.fasterxml.jackson.annotation.JsonBackReference
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

/**
 * Clasa anunt este reprezentarea obiectelor de tip anunt din baza de date, se folosesc @Table si @Entity pentru crearea automata a tabelelor si @Column pentru coloane
 */
@Table(name = "anunturi")
@Entity
data class Anunt(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "titlu")
    var titlu: String = "",

    @Column(name = "descriere")
    var descriere: String = "",

    @Column(name = "specie")
    var specie: String = "",

    @Column(name = "rasa")
    var rasa: String = "",

    @Column(name = "gen")
    @Enumerated(EnumType.STRING)
    var gen: Gen = Gen.MASCUL,

    @Column(name = "varsta")
    var varsta: Varsta = Varsta.NECUNOSCUT,

    /**
     * Redundant intrucat clasa enum Varsta contine aceste valori, dar este mai usor pentru query-uri in cazul filtrelor in functie de varsta
     */
    @Column(name = "varsta_min")
    var varstaMin: Int? = varsta.minLuni,

    /**
     * Redundant intrucat clasa enum Varsta contine aceste valori, dar este mai usor pentru query-uri in cazul filtrelor in functie de varsta
     */
    @Column(name = "varsta_max")
    var varstaMax: Int? = varsta.maxLuni,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilizator")
    @JsonBackReference
    val utilizator: Utilizator,

    @Column(name = "lista_imagini")
    var listaImagini: MutableList<String> = mutableListOf(),

    /**
     * Starea anuntului, neverificat - anuntul va aparea intr-un tabel al adminului, daca continutul este ok, trece la activ, apoi userul poate dezactiva anuntul cand doreste
     */
    @Column(name = "stare")
    @Enumerated(EnumType.STRING)
    var stare: Stare = Stare.NEVERIFICAT,

    /**
     * Data la care s-a creat anuntul
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null,

    /**
     * Data la care s-a actualizat ultima data anuntul
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_locatie")
    var locatie: Localitate,
)
