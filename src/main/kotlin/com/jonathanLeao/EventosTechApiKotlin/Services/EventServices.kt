package com.jonathanLeao.EventosTechApiKotlin.Services

import com.jonathanLeao.EventosTechApiKotlin.Entities.Event
import com.jonathanLeao.EventosTechApiKotlin.Repository.EnrollmentRepository
import com.jonathanLeao.EventosTechApiKotlin.Repository.EventRepository
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class EventServices(
    private val eventRepository: EventRepository,
    private val enrollmentRepository: EnrollmentRepository
) {

    fun findAll(pageable: Pageable): Page<Event> =
        eventRepository.findAll(pageable)

    fun findById(id: Long): Event =
        eventRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with id: $id")
        }

    fun findByName(name: String): List<Event> =
        eventRepository.findByEventNameContaining(name)

    @Transactional
    fun save(event: Event): Event {
        validateVacanciesMaxCapacity(event.vacancies, event.maxVacancies)
        return eventRepository.save(event)
    }

    @Transactional
    fun update(id: Long, event: Event): Event {
        findById(id)

        val maxVacancies = event.maxVacancies
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Max vacancies must not be null")
        val currentEnrollments = enrollmentRepository.countByEventId(id)

        if (maxVacancies < currentEnrollments) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Max vacancies cannot be less than current enrollments"
            )
        }

        event.vacancies = maxVacancies - currentEnrollments
        event.id = id
        return eventRepository.save(event)
    }

    @Transactional
    fun delete(id: Long) {
        findById(id)
        eventRepository.deleteById(id)
    }

    private fun validateVacanciesMaxCapacity(vacancies: Int?, maxVacancies: Int?) {
        val current = vacancies
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "vacancies must not be null")
        val max = maxVacancies
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "max vacancies must not be null")

        if (current <= 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "vacancies must be greater than zero")
        }
        if (current > max) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "vacancies can not exceed the limit")
        }
    }
}