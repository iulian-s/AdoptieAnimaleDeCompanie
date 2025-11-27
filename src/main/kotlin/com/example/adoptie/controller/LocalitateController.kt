package com.example.adoptie.controller

import com.example.adoptie.dto.LocalitateDTO
import com.example.adoptie.dto.toDTO
import com.example.adoptie.service.LocalitateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/localitati")
class LocalitateController (private val localitateService: LocalitateService){
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    fun intoarceLocalitatePeBazaId(@PathVariable id: Long): ResponseEntity<LocalitateDTO> {
        val localitate = localitateService.getLocalitateById(id)
        return ResponseEntity.ok(localitate?.toDTO())
    }
}