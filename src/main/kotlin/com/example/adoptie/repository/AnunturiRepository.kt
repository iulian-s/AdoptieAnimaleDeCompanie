package com.example.adoptie.repository

import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Stare
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnunturiRepository: JpaRepository<Anunt, Long> {
    /**
     * Returneaza o lista cu anunturi in functie de criteriu, activ, inactiv, neverificat
     */
    fun findByStare(stare: Stare): List<Anunt>

    /**
     * Returneaza o lista cu anunturile utilizatorului dupa id-ul acestuia
     */
    fun findByUtilizator_Id(id: Long): List<Anunt>
}