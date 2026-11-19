package com.jonathanLeao.EventosTechApiKotlin.Entities

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
    class Enrollment(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne
        var participant: Participant? = null,

        @ManyToOne
        var event: Event? = null,

        @Column(name = "enrollment_date")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        var enrollmentDate: LocalDateTime? = LocalDateTime.now()
    )
