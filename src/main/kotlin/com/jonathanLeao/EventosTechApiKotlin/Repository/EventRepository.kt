package com.jonathanLeao.EventosTechApiKotlin.Repository

// EventRepository.kt
import com.jonathanLeao.EventosTechApiKotlin.Entities.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long>{
    fun findByEventNameContaining(name: String): List<Event>
}