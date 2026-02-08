package com.example.adoptie.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/animalute")
class AnimaluteController {
    private val rase = mapOf(
        "Caine" to listOf("Labrador", "Metis", "Ciobanesc German", "Alta rasa"),
        "Pisica" to listOf("Europeana", "Siameza", "Sfinx","Persana", "Alta rasa"),
        "Rozatoare" to listOf("Hamster", "Porcusor de Guineea", "Soarece Dumbo", "Alta rasa"),
        "Soparla" to listOf("Gecko Leopard", "Iguana verde", "Dragon cu barba",  "Alta rasa"),
        "Sarpe" to listOf("Piton", "Cobra", "Cu clopotei", "Alta rasa"),
        "Paianjeni" to listOf("Tarantula", "Alta rasa")
    )

    @GetMapping
    fun getRase(): Map<String, List<String>> = rase
}