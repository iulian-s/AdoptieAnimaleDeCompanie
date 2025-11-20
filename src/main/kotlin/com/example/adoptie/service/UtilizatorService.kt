package com.example.adoptie.service

import com.example.adoptie.repository.UtilizatorRepository
import org.springframework.stereotype.Service

/**
 * Service pentru logica din spatele API-urilor specifice entitatii Utilizator
 */
@Service
class UtilizatorService(
    private val utilizatorRepository: UtilizatorRepository
) {

}