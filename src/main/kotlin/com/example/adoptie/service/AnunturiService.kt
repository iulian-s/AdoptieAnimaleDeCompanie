package com.example.adoptie.service

import com.example.adoptie.dto.AnuntDTO
import com.example.adoptie.dto.CreareAnuntDTO
import com.example.adoptie.dto.UtilizatorDTO
import com.example.adoptie.dto.toDTO
import com.example.adoptie.dto.toEntity
import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Stare
import com.example.adoptie.repository.AnunturiRepository
import com.example.adoptie.repository.LocalitateRepository
import com.example.adoptie.repository.UtilizatorRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
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
        val anunturiActive = vizualizareAnunturiActive()

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

    /**
     * Metoda de creare a unui anunt
     */
    fun creareAnunt(dto: CreareAnuntDTO, imagini: List<MultipartFile>): Anunt{
        val auth = SecurityContextHolder.getContext().authentication
        val user = utilizatorRepository.findByUsername(auth.name) ?: throw IllegalArgumentException("Nu am gasit informatii despre utilizatorul ${auth.name}")

        val imagePaths = saveImages(imagini) //returneaza lista de string
        val anunt = dto.toEntity(user, localitateRepository.findById(dto.locatieId).orElseThrow{IllegalArgumentException("Localitatea cu id ${dto.locatieId} nu exista!")})
        anunt.listaImagini = imagePaths.toMutableList()
        return anunturiRepository.save(anunt)
    }


    @Value("\${app.upload.dir}")
    lateinit var uploadDir: String

    fun saveImage(file: MultipartFile): String {
        val dir = Paths.get(uploadDir)
        Files.createDirectories(dir)

        val filename = "${System.currentTimeMillis()}_${file.originalFilename}"
        val target = dir.resolve(filename)

        Files.write(target, file.bytes)

        return "/imagini/$filename"
    }

    fun saveImages(files: List<MultipartFile>): List<String> = files.map { saveImage(it) }

    /**
     * Metoda de listare a tuturor anunturilor active
     */
    fun vizualizareAnunturiActive(): List<Anunt> = anunturiRepository.findByStare(Stare.ACTIV)

    /**
     * Metoda de listare a tuturor anunturilor
     */
    fun vizualizareAnunturi(): List<Anunt> = anunturiRepository.findAll()

    /**
     * Metoda de listare a tuturor anunturilor neverificate
     */
    fun vizualizareAnunturiNeverificate(): List<Anunt> = anunturiRepository.findByStare(Stare.NEVERIFICAT)

    /**
     * Metoda de intoarcere a unui anunt
     */
    fun vizualizareAnuntById(id: Long): Anunt = anunturiRepository.findById(id).orElseThrow { IllegalArgumentException("Anuntul cu id $id nu exista!") }

    /**
     * Metoda de editare anunt
     */
    fun editareAnunt(id: Long, dto: AnuntDTO): Anunt{
        val anunt = anunturiRepository.findById(id).orElseThrow { IllegalArgumentException("Anuntul cu id $id nu exista!") }
        editareCampuriAnunt(anunt, dto)
        return anunturiRepository.save(anunt)
    }

    fun editareCampuriAnunt(anunt: Anunt, dto: AnuntDTO): Anunt = anunt.apply {
        this.titlu = dto.titlu
        this.descriere = dto.descriere
        this.specie = dto.specie
        this.rasa = dto.rasa
        this.gen = dto.gen
        this.varsta = dto.varsta
        this.listaImagini = dto.listaImagini
        this.stare = dto.stare
        this.locatie = localitateRepository.findById(dto.locatieId).orElseThrow{IllegalArgumentException("Localitate invalida") }
    }



    /**
     *Metoda prin care utilizatorul isi modifica propriile anunturi
     */
    fun editareAnuntPropriu(id: Long, dto: AnuntDTO): Anunt{
        val auth = SecurityContextHolder.getContext().authentication
        val user = utilizatorRepository.findByUsername(auth.name) ?: throw IllegalArgumentException("Nu am gasit utilizatorul ${auth.name}")
        val anunt = anunturiRepository.findById(id).orElseThrow { IllegalArgumentException("Anuntul cu id $id nu exista!") }
        if(anunt.utilizator.id != user.id) throw IllegalAccessException("Anuntul ales nu apartine utilizatorului logat!")
        anunt.apply{
            titlu = dto.titlu
            descriere = dto.descriere
            specie = dto.specie
            rasa = dto.rasa
            gen = dto.gen
            varsta = dto.varsta
            listaImagini = dto.listaImagini
            locatie = localitateRepository.findById(dto.locatieId)
                .orElseThrow { IllegalArgumentException("Localitate invalida") }

            // permit doar schimbarea intre ACTIV si INACTIV
            if (dto.stare == Stare.ACTIV || dto.stare == Stare.INACTIV) {
                stare = dto.stare
            }
        }
        return anunturiRepository.save(anunt)
    }

    /**
     * Metoda de stergere anunt
     */
    fun stergereAnunt(id:Long) = anunturiRepository.delete(anunturiRepository.findById(id).orElseThrow { IllegalArgumentException("Anuntul cu id $id nu exista!") })

    /**
     * Metoda de intoarcere a anunturilor utilizatorului logat
     */
    fun anunturiUtilizator(): List<Anunt> {
        val auth = SecurityContextHolder.getContext().authentication
        val user = utilizatorRepository.findByUsername(auth.name) ?: throw IllegalArgumentException("Nu am gasit utilizatorul ${auth.name}")
        return (anunturiRepository.findByUtilizator_Id(user.id))
    }
}