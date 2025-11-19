package com.example.adoptie.service

import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Stare
import com.example.adoptie.model.Utilizator
import com.example.adoptie.repository.AnunturiRepository
import com.example.adoptie.repository.UtilizatorRepository
import org.springframework.stereotype.Service
import kotlin.math.pow
@Service
class AnunturiService(
    private val anunturiRepository: AnunturiRepository,
    private val utilizatorRepository: UtilizatorRepository
) {

    fun distanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0 // km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = kotlin.math.sin(dLat / 2).pow(2.0) +
                kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(Math.toRadians(lat2)) *
                kotlin.math.sin(dLon / 2).pow(2.0)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return R * c
    }

    fun getCoordinates(user: Utilizator): Pair<Double, Double>? {
        val loc = user.localitate ?: return null
        return loc.latitudine to loc.longitudine
    }

    fun getAnunturiInRaza(userId: Long, radiusKm: Double): List<Anunt> {
        val user = utilizatorRepository.findById(userId).orElseThrow()
        val coords = getCoordinates(user) ?: return emptyList()
        val(centerLat, centerLon) = coords
        val anunturiActive = anunturiRepository.findAllByStare(Stare.ACTIV)

        return anunturiActive.filter{ anunt ->
            val anuntCoords = getCoordinates(anunt.utilizator)
            anuntCoords != null &&
                    distanceKm(centerLat, centerLon, anuntCoords.first, anuntCoords.second) <= radiusKm
        }
    }

}