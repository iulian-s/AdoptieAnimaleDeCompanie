package com.example.adoptie.controller

import com.example.adoptie.dto.AnuntDTO
import com.example.adoptie.dto.LocalitateDTO
import com.example.adoptie.dto.toDTO
import com.example.adoptie.service.AnunturiService
import com.example.adoptie.service.LocalitateService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/localitati")
class LocalitateController (
    private val localitateService: LocalitateService
){
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    fun getAllLocalitati(): List<LocalitateDTO> {
        return localitateService.getLocalitati().map { it.toDTO() }
    }


    @GetMapping("/{id}")
    fun intoarceLocalitatePeBazaId(@PathVariable id: Long): ResponseEntity<LocalitateDTO> {
        val localitate = localitateService.getLocalitateById(id)
        return if(localitate != null)ResponseEntity.ok(localitate.toDTO())
        else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/judete")
    fun getJudete(): List<String> = localitateService.getLocalitati()
        .map { it.judet }.distinct().sorted()

    @GetMapping("/by-judet")
    fun getByJudet(@RequestParam judet: String): List<LocalitateDTO> =
        localitateService.getLocalitati()
            .filter { it.judet == judet }
            .map { it.toDTO() }



}