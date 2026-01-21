package com.example.adoptie.controller
import com.example.adoptie.model.LogActiune
import com.example.adoptie.repository.LogActiuneRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/logs")
class LogController(private val repo: LogActiuneRepository) {
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    fun getAllLogs(): ResponseEntity<List<LogActiune>> {
        return ResponseEntity.ok(repo.findAll())
    }
}