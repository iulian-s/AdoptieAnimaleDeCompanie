package com.example.adoptie.repository

import com.example.adoptie.model.LogActiune
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogActiuneRepository: JpaRepository<LogActiune, Long> {
}