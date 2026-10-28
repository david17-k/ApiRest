package com.example.apirest.domain

import com.example.apirest.model.Pokemon
import com.example.apirest.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
){
    suspend operator fun invoke(): List<Pokemon> = pokemonRepository.getPokemon()
}
