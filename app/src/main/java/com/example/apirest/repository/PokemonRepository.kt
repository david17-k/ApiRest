package com.example.apirest.repository

import com.example.apirest.model.Pokemon
import com.example.apirest.model.PokemonDetail

interface PokemonRepository {
    suspend fun getPokemon(): List<Pokemon>
    suspend fun getPokemonDetail(name: String): PokemonDetail
    suspend fun getPokemonByType(type: String): List<Pokemon>
    suspend fun getPokemonByGeneration(generation: Int): List<Pokemon>
}
