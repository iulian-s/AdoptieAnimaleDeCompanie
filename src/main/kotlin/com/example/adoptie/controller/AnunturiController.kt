package com.example.adoptie.controller

import com.example.adoptie.dto.AnuntDTO
import com.example.adoptie.dto.CreareAnuntDTO
import com.example.adoptie.dto.toDTO
import com.example.adoptie.service.AnunturiService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * Controller ce expune endpoint-uri pentru gestiunea actiunilor asupra entitatii Anunt
 */
@RestController
@RequestMapping("/api/anunturi")
class AnunturiController (
    val anunturiService: AnunturiService
){
    /**
     * Metoda pentru afisare anunturi intr-o raza aleasa, in functie de  locatia utilizatorului
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/razaUser")
    fun getAnunturiInRaza(@RequestParam(required = false) razaKm: Double = 5.0): ResponseEntity<List<AnuntDTO>>
    = ResponseEntity.ok(anunturiService.getAnunturiInRazaFataDeLocatiaUserului(razaKm).map{it.toDTO()})

    /**
     * Metoda pentru afisare anunturi intr-o raza aleasa, in functie de  locatia aleasa de utilizator
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/razaLocalitate")
    fun getAnunturiInRaza(
        @RequestParam localitateId: Long,
        @RequestParam razaKm: Double
    ): ResponseEntity<List<AnuntDTO>> = ResponseEntity.ok(
        anunturiService.getAnunturiInRazaFataDeLocatiaSelectata(localitateId, razaKm).map { it.toDTO() }
    )

    /**
     * Metoda pentru crearea unui anunt
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping(consumes = ["multipart/form-data"])
    fun creareAnunt(
        @RequestPart("anunt") @Valid dto: CreareAnuntDTO,
        @RequestPart("imagini") imagini: List<MultipartFile>

    ): ResponseEntity<AnuntDTO>{
        val anunt = anunturiService.creareAnunt(dto, imagini)
        return ResponseEntity.ok(anunt.toDTO())
    }

    /**
     * Metoda pentru vizualizarea tuturor anunturilor
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun vizualizareAnunturi(): ResponseEntity<List<AnuntDTO>>{
        val anunturi = anunturiService.vizualizareAnunturi()
        return ResponseEntity.ok(anunturi.map { it.toDTO() })
    }

    /**
     * Metoda pentru vizualizarea tuturor anunturilor neverificate
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/neverificate")
    fun vizualizareAnunturiNeverificate(): ResponseEntity<List<AnuntDTO>>{
        val anunturi = anunturiService.vizualizareAnunturiNeverificate()
        return ResponseEntity.ok(anunturi.map { it.toDTO() })
    }

    /**
     * Metoda pentru listarea anunturilor active, pentru ca utilizatorul sa le vada
     */
    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/active")
    fun vizualizareAnunturiActive(): ResponseEntity<List<AnuntDTO>>{
        val anunturi = anunturiService.vizualizareAnunturiActive()
        return ResponseEntity.ok(anunturi.map { it.toDTO() })
    }

    /**
     * Metoda pentru editarea unui anunt de catre admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    fun editareAnunt(@PathVariable id: Long, @RequestBody @Valid dto: AnuntDTO): ResponseEntity<AnuntDTO>{
        val anunt = anunturiService.editareAnunt(id, dto)
        return ResponseEntity.ok(anunt.toDTO())
    }

    /**
     * Metoda pentru editarea unui anunt de catre utilizatorul care l-a creat
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/eu/{id}", consumes = ["multipart/form-data"])
    fun editareAnuntPropriu(
        @PathVariable id: Long,
        @RequestPart("dto") @Valid dto: AnuntDTO,
        @RequestPart("imagini", required = false) noiImagini: List<MultipartFile>?

    ): ResponseEntity<AnuntDTO>{
        val anunt = anunturiService.editareAnuntPropriu(id, dto, noiImagini)
        return ResponseEntity.ok(anunt.toDTO())
    }

    /**
     * Metoda pentru stergerea unui anunt de catre admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun stergeAnunt(@PathVariable id: Long): ResponseEntity<String> {
        anunturiService.stergereAnunt(id)
        return ResponseEntity.ok("Anuntul cu id $id a fost sters cu succes!")
    }

    /**
     * Metoda pentru afisarea anunturilor unui utilizator
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/eu/anunturi")
    fun vizualizareAnunturiProprii(): ResponseEntity<List<AnuntDTO>>{
        val anunturi = anunturiService.anunturiUtilizator()
        return ResponseEntity.ok(anunturi.map { it.toDTO() })
    }

    /**
     * Metoda pentru returnare date dintr-un anunt selectat
     */
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    fun vizualizareAnuntById(@PathVariable id: Long): ResponseEntity<AnuntDTO>{
        val anunt = anunturiService.vizualizareAnuntById(id)
        return ResponseEntity.ok(anunt.toDTO())
    }

    @GetMapping("/judet/{numeJudet}")
    fun getAnunturiByJudet(@PathVariable numeJudet: String): ResponseEntity<List<AnuntDTO>> {
        // Filtrează anunțurile unde localitatea aparține județului respectiv
        val anunturi = anunturiService.vizualizareAnunturiActive()
            .filter { it.locatie.judet.equals(numeJudet, ignoreCase = true) }
            .map { it.toDTO() }
        return ResponseEntity.ok(anunturi)
    }
}