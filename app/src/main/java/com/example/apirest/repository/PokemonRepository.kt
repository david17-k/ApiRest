package com.example.apirest.repository

import com.example.apirest.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemon(): List<Pokemon>
}