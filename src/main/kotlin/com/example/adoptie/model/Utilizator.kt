package com.example.adoptie.model

import com.fasterxml.jackson.annotation.JsonManagedReference
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


/**
 * Clasa utilizator - folosita pentru autentificare/inregistrare si postat anunturi
 */
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
    @JoinColumn(name = "localitate_id", nullable = true)
    var localitate: Localitate? = null,

    @Column(name = "telefon")
    var telefon: String = "",

    /**
     * Imaginea de profil a utilizatorului
     */
    @Column(name = "avatar")
    var avatar: String = "",

    @CreationTimestamp
    @Column(name = "data_creare_utlizator", updatable = false)
    val dataCreare: LocalDateTime = LocalDateTime.now(),

    /**
     * Partea inversa a joinului, permite utilizatorului sa vada lista sa de anunturi
     */
    @OneToMany(mappedBy = "utilizator", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    var anunturi: MutableList<Anunt>? = mutableListOf()

)
