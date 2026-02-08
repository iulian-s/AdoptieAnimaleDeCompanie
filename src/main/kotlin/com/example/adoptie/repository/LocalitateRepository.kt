package com.example.adoptie.repository

import com.example.adoptie.model.Localitate
import org.springframework.data.jpa.repository.JpaRepository

interface LocalitateRepository: JpaRepository<Localitate, Long> {
    fun findByJudetAndNume(judet: String, nume: String): Localitate?
}