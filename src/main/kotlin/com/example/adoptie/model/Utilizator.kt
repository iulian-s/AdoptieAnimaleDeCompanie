package com.example.adoptie.model

import jakarta.persistence.CascadeType
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
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Table(name = "utilizatori")
@Entity
data class Utilizator(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "username", unique = true)
    var username: String = "",

    @Column(name = "parola")
    var parola: String = "",

    @Column(name = "email", unique = true)
    var email: String = "",

    @Column(name = "rol")
    @Enumerated(EnumType.STRING)
    var rol: Rol = Rol.USER,

    @Column(name = "nume")
    var nume: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localitate_id")
    var localitate: Localitate? = null,

    @Column(name = "telefon")
    var telefon: String = "",

    @Column(name = "avatar")
    var avatar: String = "",

    @CreationTimestamp
    @Column(name = "data_creare_utlizator", updatable = false)
    val dataCreare: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "utilizator", cascade = [CascadeType.ALL], orphanRemoval = true)
    var anunturi: MutableList<Anunt> = mutableListOf()

)
