package com.example.adoptie.repository

import com.example.adoptie.model.PasswordResetToken
import com.example.adoptie.model.Utilizator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface PasswordResetTokenRepository: JpaRepository<PasswordResetToken, Long> {
    @Query("""
       SELECT t FROM PasswordResetToken t
       JOIN FETCH t.user u
       WHERE
        t.tokenHash = :tokenHash 
        AND t.expiresAt > CURRENT_TIMESTAMP 
        AND t.used = FALSE
    """
    )
    fun findValidByTokenHash(@Param("tokenHash") tokenHash: String): PasswordResetToken?

    fun deleteByUsedTrueOrCreatedAtBefore(expiry: LocalDateTime)

    fun deleteByUser(user: Utilizator)
}