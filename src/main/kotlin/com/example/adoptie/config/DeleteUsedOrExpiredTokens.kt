package com.example.adoptie.config
import com.example.adoptie.repository.PasswordResetTokenRepository
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class DeleteUsedOrExpiredTokens(
    private val tokenRepository: PasswordResetTokenRepository
) {
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    fun cleanupTokens(){
        val twentyFourHoursAgo = LocalDateTime.now().minusHours(24)
        tokenRepository.deleteOldOrUsedTokens(twentyFourHoursAgo)
    }
}