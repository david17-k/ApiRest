package com.example.apirest.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.apirest.model.Pokemon
import com.example.apirest.model.PokemonDetail
import com.example.apirest.data.remote.PokemonDto
import com.example.apirest.navigation.PokemonPaginSource
import com.example.apirest.service.PokemonApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokemonApi
) : PokemonRepository {

    override fun getPokemon(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                PokemonPaginSource(apiService)
            }
        ).flow.map { pagingData ->

            pagingData.map { dto ->
                dto.toDomain()
            }
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
