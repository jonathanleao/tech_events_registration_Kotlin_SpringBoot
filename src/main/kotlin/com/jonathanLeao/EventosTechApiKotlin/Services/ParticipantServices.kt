package com.jonathanLeao.EventosTechApiKotlin.Services

import com.jonathanLeao.EventosTechApiKotlin.Entities.Participant
import com.jonathanLeao.EventosTechApiKotlin.Repository.ParticipantRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class ParticipantServices(
    private val participantRepository: ParticipantRepository
) {

    fun findAll(pageable: Pageable): Page<Participant> =
        participantRepository.findAll(pageable)

    fun findById(id: Long): Participant =
        participantRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found with id: $id")
        }

    fun findByName(name: String): List<Participant> =
        participantRepository.findByParticipantNameContaining(name)

    @Transactional
    fun save(participant: Participant): Participant =
        participantRepository.save(participant)

    @Transactional
    fun update(id: Long, participant: Participant): Participant {
        findById(id)
        participant.id = id
        return participantRepository.save(participant)
    }

    @Transactional
    fun delete(id: Long) {
        findById(id)
        participantRepository.deleteById(id)
    }
}