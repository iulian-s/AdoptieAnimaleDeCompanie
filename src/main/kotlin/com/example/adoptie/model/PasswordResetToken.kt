package com.example.adoptie.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(
    name = "password_reset_tokens",
    indexes = [
        Index(name = "idx_token_user", columnList = "user_id")
    ]
)
@Entity
class PasswordResetToken(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: Utilizator,

    @Column(name = "token_hash", unique = true, nullable = false)
    var tokenHash: String,

    @Column(name = "expires_at", updatable = false)
//    var expiresAt: LocalDateTime = LocalDateTime.now().plusMinutes(15),
    var expiresAt: LocalDateTime? = null,

    @Column(name = "used")
    var used: Boolean = false,

    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime? = null,
)