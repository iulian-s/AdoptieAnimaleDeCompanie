package com.example.adoptie.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

import com.resend.*
import com.resend.services.emails.model.CreateEmailOptions

@Service
class EmailService(
    @Value("\${resend.api.key}") private val apiKey: String,
    @Value("\${resend.from.email}") private val fromEmail: String,
) {
    private val resend = Resend(apiKey)
    private fun sendEmail(to: String, subject: String, content: String) {
        val params = CreateEmailOptions.builder()
            .from(fromEmail)
            .to(to)
            .subject(subject)
            .text(content)
            .build()

        try {
            resend.emails().send(params)
        } catch (e: Exception) {
            println("Error sending email: ${e.message}")
        }
    }

    @Async
    fun sendPasswordResetEmail(to: String, token: String){
        val subject = "Resetare parola"
        val text = "Codul tau pentru resetarea parolei este: $token\nAcest email a fost trimis automat de catre aplicatia unui proiect personal, non-profit, iar introducerea datelor personale este riscanta si nerecomandata!"
        sendEmail(to, subject, text)
        println("Sent password reset email to: $to")
    }

    @Async
    fun sendRegistrationEmail(to: String){
        val subject = "Inregistrare $to"
        val text = "Multumim pentru crearea contului pe platforma noastra!\nAcest email a fost trimis automat de catre aplicatia unui proiect personal, non-profit, iar introducerea datelor personale este riscanta si nerecomandata!"
        sendEmail(to, subject, text)
        println("Sent registration email to: $to")
    }
}