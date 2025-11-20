package com.example.adoptie.controller

import com.example.adoptie.dto.AnuntDTO
import com.example.adoptie.model.Anunt
import com.example.adoptie.service.AnunturiService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/anunturi")
class AnunturiController (
    val anunturiService: AnunturiService
){
    @GetMapping("/raza")
    fun getAnunturiInRaza(
        @RequestParam userId: Long,
        @RequestParam radiusKm: Double
    ): ResponseEntity<List<AnuntDTO>>{
        return ResponseEntity.ok(anunturiService.getAnunturiInRaza(userId, radiusKm))
    }
}