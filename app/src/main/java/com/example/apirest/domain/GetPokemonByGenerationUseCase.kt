package com.example.apirest.domain

import com.example.apirest.model.Pokemon
import com.example.apirest.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonByGenerationUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
){
    suspend operator fun invoke(generation: Int): List<Pokemon> = pokemonRepository.getPokemonByGeneration(generation)
}
