package com.jonathanLeao.EventosTechApiKotlin.Entities

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var eventName: String? = null,
    var description: String? = null,
    var local: String? = null,
    var category: String? = null,
    var vacancies: Int? = null,
    var maxVacancies: Int? = null,

    @Column(name = "event_date_and_hours")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    var eventDateAndHours: LocalDateTime? = null
)