package com.jonathanLeao.EventosTechApiKotlin.Controllers

import com.jonathanLeao.EventosTechApiKotlin.Entities.Event
import com.jonathanLeao.EventosTechApiKotlin.Services.EventServices
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
class EventController(
    private val eventServices: EventServices
) {

    @GetMapping
    fun findAll(pageable: Pageable): ResponseEntity<Page<Event>> =
        ResponseEntity.ok(eventServices.findAll(pageable))

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Event> =
        ResponseEntity.ok(eventServices.findById(id))

    @GetMapping("/search")
    fun findByName(@RequestParam name: String): ResponseEntity<List<Event>> =
        ResponseEntity.ok(eventServices.findByName(name))

    @PostMapping
    fun save(@Valid @RequestBody event: Event): ResponseEntity<Event> =
        ResponseEntity.status(HttpStatus.CREATED).body(eventServices.save(event))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody event: Event
    ): ResponseEntity<Event> =
        ResponseEntity.ok(eventServices.update(id, event))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        eventServices.delete(id)
        return ResponseEntity.noContent().build()
    }
}