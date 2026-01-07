package com.example.adoptie.controller

import com.example.adoptie.dto.CreareUtilizatorDTO
import com.example.adoptie.model.LogActiune
import com.example.adoptie.repository.LogActiuneRepository
import com.example.adoptie.service.DetaliiUtilizatorService
import com.example.adoptie.service.JwtService
import com.example.adoptie.service.UtilizatorService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")

class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val detaliiUtilizatorService: DetaliiUtilizatorService,
    private val jwtService: JwtService,
    private val utilizatorService: UtilizatorService,
    private val logRepo: LogActiuneRepository
) {
    data class AuthRequest(val username: String, val parola: String)
    data class AuthResponse(val token: String)

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        return try {
            val auth = UsernamePasswordAuthenticationToken(request.username, request.parola)
            authenticationManager.authenticate(auth)

            val user = detaliiUtilizatorService.loadUserByUsername(request.username)
            val jwt = jwtService.generateToken(user)

            ResponseEntity.ok(AuthResponse(jwt))
        } catch (ex: AuthenticationException) {
            logRepo.save(LogActiune(
                username = request.username,
                endpoint = "/api/auth/login",
                metoda = "POST",
                status = 401
            ))
            ResponseEntity.status(401).body(AuthResponse("Username/parola gresite!"))
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody dto: CreareUtilizatorDTO): ResponseEntity<*> {
        return try{
            val user = utilizatorService.inregistrare(dto)
            val userDetails = detaliiUtilizatorService.loadUserByUsername(user.username)
            val jwt = jwtService.generateToken(userDetails)
            ResponseEntity.ok(AuthResponse(jwt))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(409).body(mapOf("error" to e.message))
        }


    }
}