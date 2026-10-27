package com.example.apirest.domain

import com.example.apirest.model.Pokemon
import com.example.apirest.repository.PokemonRepository
import javax.inject.Inject

class GetReportUseCase @Inject constructor(
    private val reportRepository: PokemonRepository
){
    suspend operator fun invoke(): List<Pokemon> = reportRepository.getPokemon()
}