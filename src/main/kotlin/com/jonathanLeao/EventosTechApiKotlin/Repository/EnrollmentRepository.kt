package com.jonathanLeao.EventosTechApiKotlin.Repository

import com.jonathanLeao.EventosTechApiKotlin.Entities.Enrollment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EnrollmentRepository : JpaRepository<Enrollment, Long>{
    fun countByEventId(eventId: Long): Int
}