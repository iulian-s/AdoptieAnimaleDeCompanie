package com.example.adoptie.config

import com.example.adoptie.dto.LocalitateDTO
import com.example.adoptie.model.Localitate
import com.example.adoptie.repository.LocalitateRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component


/**
 * Clasa de tip @Component care verifica daca exista localitati in baza de date, iar daca nu, incarca fisierul localitati.json in baza de date
 */
@Component
class LocalitatiJsonInSQL(
    private val localitateRepository: LocalitateRepository,
    private val mapper: ObjectMapper
) {

    @PostConstruct
    fun load() {
        if (localitateRepository.count() > 0) return

        val resource = ClassPathResource("localitati.json")
        val input = resource.inputStream

        val lista = mapper.readValue(input, Array<LocalitateDTO>::class.java).toList()

        val entities = lista.map {
            Localitate(
                nume = it.nume,
                diacritice = it.diacritice,
                judet = it.judet,
                zip = it.zip,
                latitudine = it.lat,
                longitudine = it.lng
            )
        }

        localitateRepository.saveAll(entities)
    }
}

