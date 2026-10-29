package com.example.apirest.repository

import androidx.paging.PagingData
import com.example.apirest.model.Pokemon
import com.example.apirest.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
     fun getPokemon(): Flow<PagingData<Pokemon>>
    suspend fun getPokemonDetail(name: String): PokemonDetail
    suspend fun getPokemonByType(type: String): List<Pokemon>
    suspend fun getPokemonByGeneration(generation: Int): List<Pokemon>


}
