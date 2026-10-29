package com.example.apirest.domain

import androidx.paging.PagingData
import com.example.apirest.model.Pokemon
import com.example.apirest.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
){
     operator fun invoke(): Flow<PagingData<Pokemon>> = pokemonRepository.getPokemon()
}
