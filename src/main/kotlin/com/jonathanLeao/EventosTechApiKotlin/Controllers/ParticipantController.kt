package com.jonathanLeao.EventosTechApiKotlin.Controllers

import com.jonathanLeao.EventosTechApiKotlin.Entities.Participant
import com.jonathanLeao.EventosTechApiKotlin.Services.ParticipantServices
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/participants")
class ParticipantController(
    private val participantServices: ParticipantServices
) {

    @GetMapping
    fun findAll(pageable: Pageable): ResponseEntity<Page<Participant>> =
        ResponseEntity.ok(participantServices.findAll(pageable))

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Participant> =
        ResponseEntity.ok(participantServices.findById(id))

    @GetMapping("/search")
    fun findByName(@RequestParam name: String): ResponseEntity<List<Participant>> =
        ResponseEntity.ok(participantServices.findByName(name))

    @PostMapping
    fun save(@Valid @RequestBody participant: Participant): ResponseEntity<Participant> =
        ResponseEntity.status(HttpStatus.CREATED).body(participantServices.save(participant))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody participant: Participant
    ): ResponseEntity<Participant> =
        ResponseEntity.ok(participantServices.update(id, participant))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        participantServices.delete(id)
        return ResponseEntity.noContent().build()
    }
}