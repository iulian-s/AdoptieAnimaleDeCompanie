package com.example.adoptie.config

import com.example.adoptie.service.DetaliiUtilizatorService
import com.example.adoptie.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filtru jwt pentru autentificare
 */
@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userDetailsService: DetaliiUtilizatorService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)
        try{
        val username = jwtService.extractUsername(jwt)

        if(username != null && SecurityContextHolder.getContext().authentication == null){
            val userDetails = userDetailsService.loadUserByUsername(username)
            if(jwtService.isTokenValid(jwt, userDetails)){
                //extract role from jwt
                val rolefromToken = jwtService.extractRole(jwt) // ROLE_ADMIN, ROLE_USER
                val authorities = listOf(SimpleGrantedAuthority(rolefromToken))

                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        } catch (ex: io.jsonwebtoken.ExpiredJwtException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        } catch (ex: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }
        filterChain.doFilter(request, response)
    }
}