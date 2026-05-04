package com.example.adoptie.repository

import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Stare
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AnunturiRepository: JpaRepository<Anunt, Long> {
    /**
     * Returneaza o lista cu anunturi in functie de criteriu, activ, inactiv, neverificat
     */
    fun findByStare(stare: Stare): List<Anunt>

    /**
     * Returneaza o lista cu anunturile utilizatorului dupa id-ul acestuia
     */
    fun findByUtilizator_Id(id: Long?): List<Anunt>

    @Query("SELECT a FROM Anunt a WHERE a.createdAt >= :dataStart")
    fun findAnunturiRecente(dataStart: LocalDateTime): List<Anunt>
}