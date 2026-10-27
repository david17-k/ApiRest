package com.example.apirest.repository

import com.example.apirest.model.Pokemon
import com.example.apirest.service.PokemonApi
import javax.inject.Inject


class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokemonApi
) : PokemonRepository {

    override suspend fun getPokemon(): List<Pokemon> {
        return apiService.getPokemon().results.map { pokemonDto ->
            Pokemon(
                id = pokemonDto.id,
                name = pokemonDto.name,
                photoUrl = pokemonDto.photoUrl
            )
        }
    }
}