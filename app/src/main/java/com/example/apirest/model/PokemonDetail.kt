package com.example.apirest.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val photoUrl: String,
    val height: Double,      // En metros
    val weight: Double,      // En kilogramos
    val types: List<String>  // Ej: fire, water
)
