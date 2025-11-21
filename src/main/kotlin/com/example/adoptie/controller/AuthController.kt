package com.example.adoptie.controller

import com.example.adoptie.dto.CreareUtilizatorDTO
import com.example.adoptie.service.DetaliiUtilizatorService
import com.example.adoptie.service.JwtService
import com.example.adoptie.service.UtilizatorService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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
    private val utilizatorService: UtilizatorService
) {
    data class AuthRequest(val username: String, val parola: String)
    data class AuthResponse(val token: String)

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        val auth  = UsernamePasswordAuthenticationToken(request.username, request.parola)
        authenticationManager.authenticate(auth)

        val user = detaliiUtilizatorService.loadUserByUsername(request.username)
        val jwt = jwtService.generateToken(user)
        return ResponseEntity.ok(AuthResponse(jwt))
    }

    @PostMapping("/register")
    fun register(@RequestBody dto: CreareUtilizatorDTO): ResponseEntity<AuthResponse> {
        val user = utilizatorService.inregistrare(dto)
        val userDetails = detaliiUtilizatorService.loadUserByUsername(user.username)
        val jwt = jwtService.generateToken(userDetails)
        return ResponseEntity.ok(AuthResponse(jwt))
    }
}