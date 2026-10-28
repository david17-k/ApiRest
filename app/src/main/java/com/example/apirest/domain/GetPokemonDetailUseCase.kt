package com.example.apirest.domain

import com.example.apirest.model.PokemonDetail
import com.example.apirest.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
){
    suspend operator fun invoke(name: String): PokemonDetail = pokemonRepository.getPokemonDetail(name)
}
