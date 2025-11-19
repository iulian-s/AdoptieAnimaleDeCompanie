package com.example.adoptie.repository

import com.example.adoptie.model.Anunt
import com.example.adoptie.model.Utilizator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UtilizatorRepository: JpaRepository<Utilizator, Long> {
}