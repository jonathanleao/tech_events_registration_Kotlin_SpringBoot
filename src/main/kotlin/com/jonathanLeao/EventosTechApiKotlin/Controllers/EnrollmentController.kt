package com.jonathanLeao.EventosTechApiKotlin.Controllers

import com.jonathanLeao.EventosTechApiKotlin.Entities.Enrollment
import com.jonathanLeao.EventosTechApiKotlin.Services.EnrollmentServices
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enrollments")
class EnrollmentController(
    private val enrollmentServices: EnrollmentServices
) {

    @GetMapping
    fun findAll(pageable: Pageable): ResponseEntity<Page<Enrollment>> =
        ResponseEntity.ok(enrollmentServices.findAll(pageable))

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Enrollment> =
        ResponseEntity.ok(enrollmentServices.findById(id))

    @PostMapping
    fun save(@RequestBody enrollment: Enrollment): ResponseEntity<Enrollment> =
        ResponseEntity.status(HttpStatus.CREATED).body(enrollmentServices.save(enrollment))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody enrollment: Enrollment
    ): ResponseEntity<Enrollment> =
        ResponseEntity.ok(enrollmentServices.update(id, enrollment))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        enrollmentServices.delete(id)
        return ResponseEntity.noContent().build()
    }
}