package com.example.adoptie.config

import com.example.adoptie.model.LogActiune
import com.example.adoptie.repository.LogActiuneRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.Exception

/**
 * Logarea propriu zisa a actiunilor - user - tipul metodei - endpointul
 */
@Component
class LogActiuneInterceptor(private val logRepo: LogActiuneRepository): HandlerInterceptor {
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val auth = SecurityContextHolder.getContext().authentication
        val user = auth.name ?: "Anonim"

        if (!request.requestURI.contains("/api/auth/login") &&
                (request.method == "POST" || request.method == "PUT" || request.method == "DELETE")) {
            logRepo.save(
                LogActiune(
                    username = user,
                    endpoint = request.requestURI,
                    metoda = request.method,
                    status = response.status
                )
            )
        }
    }
}