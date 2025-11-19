package com.example.adoptie.repository

import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Stare
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnunturiRepository: JpaRepository<Anunt, Long> {
    fun findAllByStare(stare: Stare): List<Anunt>
}