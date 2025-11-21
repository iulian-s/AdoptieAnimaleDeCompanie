package com.example.adoptie.service

import com.example.adoptie.config.DetaliiUtilizator
import com.example.adoptie.repository.UtilizatorRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Clasa de tip service care incarca datele utilizatorului daca acesta exista
 */
@Service
class DetaliiUtilizatorService(private val utilizatorRepository: UtilizatorRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): DetaliiUtilizator{
        val user = utilizatorRepository.findByUsername(username) ?: throw UsernameNotFoundException("Nu s-a gasit utilizatorul ${username}.")
        return DetaliiUtilizator(user)
    }
}