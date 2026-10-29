package com.example.apirest.features.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
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
    val selectedType: String? = null,
    val selectedGeneration: Int? = null,
    val filteredPokemon: List<Pokemon>? = null,
    val isFiltering: Boolean = false,
    val filterError: String? = null
)

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonByTypeUseCase: GetPokemonByTypeUseCase,
    private val getPokemonByGenerationUseCase: GetPokemonByGenerationUseCase,
    private val resources: ResourceProvider,
    networkMonitor: NetworkMonitor
) : ViewModel() {
   val pokemonList=getPokemonListUseCase().cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    // Estado de la conexión: true = online, false = offline
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline

    init {
        loadPokemon()
    }

    // Lista sin filtros
    fun loadPokemon() {
        _uiState.value = PokemonUiState()
    }

    // Filtra por tipo (Ej: fire, water)
    fun filterByType(type: String) {
        _uiState.update {
            it.copy(
                selectedType = type,
                selectedGeneration = null,
                filteredPokemon = null,
                isFiltering = true,
                filterError = null
            )
        }

        viewModelScope.launch {
            try {
                val result = getPokemonByTypeUseCase(type)

                _uiState.update {
                    if (it.selectedType == type) {
                        it.copy(
                            filteredPokemon = result,
                            isFiltering = false
                        )
                    } else {
                        it
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    if (it.selectedType == type) {
                        it.copy(
                            isFiltering = false,
                            filterError = e.localizedMessage
                                ?: "Error al filtrar por tipo"
                        )
                    } else {
                        it
                    }
                }
            }
        }
    }

    // Filtra por generación (Ej: 1, 2, 3)
    fun filterByGeneration(generation: Int) {
        _uiState.update {
            it.copy(
                selectedType = null,
                selectedGeneration = generation,
                filteredPokemon = null,
                isFiltering = true,
                filterError = null
            )
        }

        viewModelScope.launch {
            try {
                val result =
                    getPokemonByGenerationUseCase(generation)

                _uiState.update {
                    if (it.selectedGeneration == generation) {
                        it.copy(
                            filteredPokemon = result,
                            isFiltering = false
                        )
                    } else {
                        it
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    if (it.selectedGeneration == generation) {
                        it.copy(
                            isFiltering = false,
                            filterError = e.localizedMessage
                                ?: "Error al filtrar por generación"
                        )
                    } else {
                        it
                    }
                }
            }
        }
    }


    // Vuelve a hacer la última petición (para el botón de reintentar)
    // Reintentar el filtro activo
    fun retry() {
        val state = _uiState.value

        when {
            state.selectedType != null ->
                filterByType(state.selectedType)

            state.selectedGeneration != null ->
                filterByGeneration(state.selectedGeneration)

            else -> loadPokemon()
        }
    }
}

    // Hace la petición y guarda el resultado en el estado



