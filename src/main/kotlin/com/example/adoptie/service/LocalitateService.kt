package com.example.adoptie.service

import com.example.adoptie.model.Localitate
import com.example.adoptie.repository.LocalitateRepository
import org.springframework.stereotype.Service

@Service
class LocalitateService(private val localitateRepository: LocalitateRepository) {
    /**
     * cauta id ul localitatii dupa judet si nume
     * Returneaza null daca nu gaseste
     */
    fun getLocalitateByJudetAndNume(judet: String?, nume: String?): Localitate? {
        if (judet.isNullOrBlank() || nume.isNullOrBlank()) return null
        return localitateRepository.findByJudetAndNume(judet, nume)
    }

    /**
     * returneaza locatia pe baza id ului
     */
    fun getLocalitateById(id: Long): Localitate? = localitateRepository.findById(id).orElse(null)

    fun getLocalitati(): List<Localitate> = localitateRepository.findAll().sortedBy {it.nume}
}