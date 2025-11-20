package com.example.adoptie.controller

import com.example.adoptie.dto.AnuntDTO
import com.example.adoptie.dto.toDTO
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
    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/razaUser")
    fun getAnunturiInRaza(@RequestParam razaKm: Double): ResponseEntity<List<AnuntDTO>>
    = ResponseEntity.ok(anunturiService.getAnunturiInRazaFataDeLocatiaUserului(razaKm).map{it.toDTO()})

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/razaLocalitate")
    fun getAnunturiInRaza(
        @RequestParam localitateId: Long,
        @RequestParam razaKm: Double
    ): ResponseEntity<List<AnuntDTO>> = ResponseEntity.ok(
        anunturiService.getAnunturiInRazaFataDeLocatiaSelectata(localitateId, razaKm).map { it.toDTO() }
    )

}