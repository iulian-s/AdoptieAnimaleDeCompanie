package com.example.adoptie.service

import com.example.adoptie.dto.UtilizatorDTO
import com.example.adoptie.dto.toDTO
import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Stare
import com.example.adoptie.repository.AnunturiRepository
import com.example.adoptie.repository.LocalitateRepository
import com.example.adoptie.repository.UtilizatorRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import kotlin.math.pow

/**
 * Service pentru logica din spatele API-urilor specifice entitatii Anunt
 */
@Service
class AnunturiService(
    private val anunturiRepository: AnunturiRepository,
    private val utilizatorRepository: UtilizatorRepository,
    private val localitateRepository: LocalitateRepository,
) {

    /**
     * Functie Haversine pentru afisarea anunturilor pe o raza selectata
     */
    fun distanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = kotlin.math.sin(dLat / 2).pow(2.0) +
                kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(Math.toRadians(lat2)) *
                kotlin.math.sin(dLon / 2).pow(2.0)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return r * c
    }

    /**
     * Extragerea coordonatelor din dto de tip utilizator
     */
    fun getCoordonate(dto: UtilizatorDTO): Pair<Double, Double>? {
        val loc = localitateRepository.findById(dto.localitateId).orElseThrow{IllegalArgumentException("Localitatea cu id ${dto.localitateId} nu exista!") }
        return loc.latitudine to loc.longitudine
    }

    /**
     * Return lista de anunturi active la locatia utilizatorului + raza
     */
    fun getAnunturiInRazaFataDeLocatiaUserului(razaKm: Double): List<Anunt> {
        val auth = SecurityContextHolder.getContext().authentication
        val user = utilizatorRepository.findByUsername(auth.name)?.toDTO()?: throw IllegalArgumentException("Nu am gasit informatii despre locatia utilizatorului ${auth.name}")
        val userCoords = getCoordonate(user) ?: return emptyList()
        val(centerLat, centerLon) = userCoords
        val anunturiActive = anunturiRepository.findByStare(Stare.ACTIV)

        return anunturiActive.filter{ anunt ->
            val(lat, lon) = anunt.locatie.let{it.latitudine to it.longitudine }

            distanceKm(centerLat, centerLon, lat, lon) <= razaKm
        }
    }

    /**
     * Return lista de anunturi active pentru localitatea selectata + raza
     */
    fun getAnunturiInRazaFataDeLocatiaSelectata(localitateId: Long, razaKm: Double): List<Anunt> {
        val localitate = localitateRepository.findById(localitateId)
            .orElseThrow { IllegalArgumentException("Localitate invalida") }

        val centerLat = localitate.latitudine
        val centerLon = localitate.longitudine

        val anunturiActive = anunturiRepository.findByStare(Stare.ACTIV)

        return anunturiActive.filter { anunt ->
            val (lat, lon) = anunt.locatie.let { it.latitudine to it.longitudine }
            distanceKm(centerLat, centerLon, lat, lon) <= razaKm
        }
    }

}