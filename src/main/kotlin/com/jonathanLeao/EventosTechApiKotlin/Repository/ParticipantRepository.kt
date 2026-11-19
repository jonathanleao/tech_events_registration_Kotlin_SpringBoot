package com.jonathanLeao.EventosTechApiKotlin.Repository

// ParticipantRepository.kt
import com.jonathanLeao.EventosTechApiKotlin.Entities.Participant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipantRepository : JpaRepository<Participant, Long> {
    fun findByParticipantNameContaining(name: String): List<Participant>
}