package com.example.apirest.model

import java.time.LocalDate

data class Report(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val photoUrl: String,
    val ownerId: String,
    val date: LocalDate

)