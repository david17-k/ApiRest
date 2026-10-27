package com.example.apirest.features.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.apirest.model.PokemonDto
import com.example.apirest.service.PokemonApi


@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonApi: PokemonApi
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<PokemonDto>>(emptyList())
    val pokemonList: StateFlow<List<PokemonDto>> = _pokemonList.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadPokemon()
    }

    private fun loadPokemon() {
        viewModelScope.launch {
            try {
                _pokemonList.value = pokemonApi.getPokemon().results
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "No se pudieron cargar los Pokémon"
            }
        }
    }
}