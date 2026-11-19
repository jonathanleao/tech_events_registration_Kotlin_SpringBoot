package com.jonathanLeao.EventosTechApiKotlin.Services

import com.jonathanLeao.EventosTechApiKotlin.Entities.Enrollment
import com.jonathanLeao.EventosTechApiKotlin.Entities.Event
import com.jonathanLeao.EventosTechApiKotlin.Repository.EnrollmentRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class EnrollmentServices(
    private val enrollmentRepository: EnrollmentRepository,
    private val participantServices: ParticipantServices,
    private val eventServices: EventServices
) {

    fun findAll(pageable: Pageable): Page<Enrollment> =
        enrollmentRepository.findAll(pageable)

    fun findById(id: Long): Enrollment =
        enrollmentRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found with id: $id")
        }

    @Transactional
    fun save(enrollment: Enrollment): Enrollment {
        val eventId = enrollment.event?.id
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "event.id é obrigatório")
        val participantId = enrollment.participant?.id
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "participant.id é obrigatório")

        val event = eventServices.findById(eventId)
        validateEventHasNotOccurred(event)

        val participant = participantServices.findById(participantId)

        decreaseVacancies(event)

        val entity = Enrollment(
            participant = participant,
            event = event,
            enrollmentDate = LocalDateTime.now()
        )
        return enrollmentRepository.save(entity)
    }

    @Transactional
    fun update(id: Long, enrollment: Enrollment): Enrollment {
        val existing = findById(id)

        val eventId = enrollment.event?.id
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "event.id é obrigatório")
        val participantId = enrollment.participant?.id
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "participant.id é obrigatório")

        val event = eventServices.findById(eventId)
        validateEventHasNotOccurred(event)

        val participant = participantServices.findById(participantId)

        val oldEvent = existing.event
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Enrollment sem event")

        if (oldEvent.id != event.id) {
            increaseVacancies(oldEvent)
            decreaseVacancies(event)
            existing.enrollmentDate = LocalDateTime.now()
        }

        existing.event = event
        existing.participant = participant
        return enrollmentRepository.save(existing)
    }

    @Transactional
    fun delete(id: Long) {
        val enrollment = findById(id)
        val event = enrollment.event
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Enrollment sem event")
        increaseVacancies(event)
        enrollmentRepository.deleteById(id)
    }

    private fun decreaseVacancies(event: Event) {
        val current = event.vacancies
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "vacancies must not be null")

        if (current <= 0) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "No vacancies available for this event")
        }
        event.vacancies = current - 1
    }

    private fun increaseVacancies(event: Event) {
        val current = event.vacancies
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "vacancies must not be null")
        val max = event.maxVacancies
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "max vacancies must not be null")

        val newVacancies = current + 1
        if (newVacancies > max) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "vacancies can not exceed the limit")
        }
        event.vacancies = newVacancies
    }

    private fun validateEventHasNotOccurred(event: Event) {
        val date = event.eventDateAndHours
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Event sem data")

        if (date.isBefore(LocalDateTime.now())) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "You can't subscribe, the event has already occurred"
            )
        }
    }
}