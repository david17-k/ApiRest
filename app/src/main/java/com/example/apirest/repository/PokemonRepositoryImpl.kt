package com.example.apirest.repository

import com.example.apirest.model.Pokemon
import com.example.apirest.model.PokemonDetail
import com.example.apirest.model.PokemonDto
import com.example.apirest.service.PokemonApi
import javax.inject.Inject


class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokemonApi
) : PokemonRepository {

    override suspend fun getPokemon(): List<Pokemon> {
        return apiService.getPokemon().results.map { pokemonDto ->
            toPokemon(pokemonDto)
        }
    }

    override suspend fun getPokemonDetail(name: String): PokemonDetail {
        val dto = apiService.getPokemonDetail(name)
        return PokemonDetail(
            id = dto.id,
            name = dto.name,
            photoUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${dto.id}.png",
            height = dto.height / 10.0, // La API lo da en decímetros
            weight = dto.weight / 10.0, // La API lo da en hectogramos
            types = dto.types.map { slot -> slot.type.name }
        )
    }

    override suspend fun getPokemonByType(type: String): List<Pokemon> {
        return apiService.getPokemonByType(type).pokemon.map { slot ->
            toPokemon(slot.pokemon)
        }
    }

    override suspend fun getPokemonByGeneration(generation: Int): List<Pokemon> {
        return apiService.getPokemonByGeneration(generation).pokemonSpecies
            .map { pokemonDto -> toPokemon(pokemonDto) }
            .sortedBy { pokemon -> pokemon.id }
    }

    // Convierte el DTO de la API al modelo de dominio
    private fun toPokemon(pokemonDto: PokemonDto): Pokemon {
        return Pokemon(
            id = pokemonDto.id,
            name = pokemonDto.name,
            photoUrl = pokemonDto.photoUrl
        )
    }
}
