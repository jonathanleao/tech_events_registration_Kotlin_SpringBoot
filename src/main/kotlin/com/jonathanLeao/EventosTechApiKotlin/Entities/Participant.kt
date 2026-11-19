package com.jonathanLeao.EventosTechApiKotlin.Entities

import jakarta.persistence.*
import jakarta.validation.constraints.Email

@Entity
class Participant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var participantName: String? = null,

    @field:Email
    var email: String? = null,

    var phoneNumber: String? = null,
    var institution: String? = null
)