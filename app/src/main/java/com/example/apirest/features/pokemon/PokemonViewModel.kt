package com.example.apirest.features.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apirest.R
import com.example.apirest.core.util.NetworkMonitor
import com.example.apirest.core.util.RequestResult
import com.example.apirest.core.util.ResourceProvider
import com.example.apirest.domain.GetPokemonByGenerationUseCase
import com.example.apirest.domain.GetPokemonByTypeUseCase
import com.example.apirest.domain.GetPokemonListUseCase
import com.example.apirest.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// El estado de la pantalla: la lista, el filtro elegido y el resultado de la petición
data class PokemonUiState(
    val pokemonList: List<Pokemon> = emptyList(),
    val selectedType: String? = null,
    val selectedGeneration: Int? = null,
    val result: RequestResult? = null
)

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonByTypeUseCase: GetPokemonByTypeUseCase,
    private val getPokemonByGenerationUseCase: GetPokemonByGenerationUseCase,
    private val resources: ResourceProvider,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    // Estado de la conexión: true = online, false = offline
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline

    init {
        loadPokemon()
    }

    // Lista sin filtros
    fun loadPokemon() {
        _uiState.update { it.copy(selectedType = null, selectedGeneration = null) }
        request { getPokemonListUseCase() }
    }

    // Filtra por tipo (Ej: fire, water)
    fun filterByType(type: String) {
        _uiState.update { it.copy(selectedType = type, selectedGeneration = null) }
        request { getPokemonByTypeUseCase(type) }
    }

    // Filtra por generación (Ej: 1, 2, 3)
    fun filterByGeneration(generation: Int) {
        _uiState.update { it.copy(selectedType = null, selectedGeneration = generation) }
        request { getPokemonByGenerationUseCase(generation) }
    }

    // Vuelve a hacer la última petición (para el botón de reintentar)
    fun retry() {
        val type = _uiState.value.selectedType
        val generation = _uiState.value.selectedGeneration
        if (type != null) {
            filterByType(type)
        } else if (generation != null) {
            filterByGeneration(generation)
        } else {
            loadPokemon()
        }
    }

    // Hace la petición y guarda el resultado en el estado
    private fun request(call: suspend () -> List<Pokemon>) {
        viewModelScope.launch {
            _uiState.update { it.copy(result = RequestResult.Loading) }
            try {
                val list = call()
                _uiState.update {
                    it.copy(
                        pokemonList = list,
                        result = RequestResult.Success(resources.getString(R.string.reports_loaded))
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        result = RequestResult.Failure(
                            e.message ?: resources.getString(R.string.reports_loaded_error)
                        )
                    )
                }
            }
        }
    }
}
