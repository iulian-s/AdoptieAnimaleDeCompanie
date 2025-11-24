package com.example.adoptie.controller

import com.example.adoptie.dto.EditareUtilizatorDTO
import com.example.adoptie.dto.UtilizatorDTO
import com.example.adoptie.dto.toDTO

import com.example.adoptie.service.UtilizatorService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/utilizator")
class UtilizatorController(private val utilizatorService: UtilizatorService) {
    /**
     * API dedicat adminului pentru listarea tuturor utilizatorilor
     */
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun listareUtilizatori(): ResponseEntity<List<UtilizatorDTO>>{
        val utilizatori = utilizatorService.listareUtilizatori()
        return ResponseEntity.ok(utilizatori.map{it.toDTO()})
    }
    /**
     * API dedicat adminului pentru listarea informatiilor unui utilizator selectat pe baza id ului
     */
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    fun citireUtilizatorPeBazaId(@PathVariable id: Long): ResponseEntity<UtilizatorDTO>{
        val utilizator = utilizatorService.citireUtilizatorById(id)
        return ResponseEntity.ok(utilizator.toDTO())
    }
    /**
     * API dedicat adminului pentru listarea informatiilor unui utilizator selectat pe baza id ului
     */
    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun stergereUtilizatorPeBazaId(@PathVariable id: Long): ResponseEntity<String>{
        utilizatorService.stergereUtilizator(id)
        return ResponseEntity.ok("Utilizatorul cu id $id a fost sters cu succes.")
    }

    /**
     * API dedicat utilizatorului pentru a vizualiza informatiile personale
     */
    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/eu")
    fun infoUtilizator(): ResponseEntity<UtilizatorDTO>{
        val user = utilizatorService.infoUtilizator()
        return ResponseEntity.ok(user.toDTO())
    }

    /**
     * API dedicat utilizatorului pentru a edita informatiile personale
     */
    //@PreAuthorize("hasRole('USER')")
    @PutMapping("/edit")
    fun editareInfoUtilizator(@RequestBody @Valid dto: EditareUtilizatorDTO): ResponseEntity<UtilizatorDTO>{
        val user = utilizatorService.editInfoUtilizator(dto)
        return ResponseEntity.ok(user.toDTO())
    }
    /**
     * API dedicat utilizatorului pentru a-si sterge contul
     */
    //@PreAuthorize("hasRole('USER')")
    @DeleteMapping
    fun stergereContUtilizator(): ResponseEntity<String> {
        utilizatorService.stergeContUtilizator()
        return ResponseEntity.ok("Contul a fost sters cu succes.")
    }
}