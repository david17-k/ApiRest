package com.example.apirest.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apirest.R
import com.example.apirest.core.util.RequestResult
import com.example.apirest.core.util.ResourceProvider
import com.example.apirest.domain.GetPokemonDetailUseCase
import com.example.apirest.model.PokemonDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// El estado de la pantalla: el Pokémon y el resultado de la petición
data class DetailUiState(
    val pokemon: PokemonDetail? = null,
    val result: RequestResult? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadPokemon(name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(result = RequestResult.Loading) }
            try {
                val pokemon = getPokemonDetailUseCase(name)
                _uiState.update {
                    it.copy(
                        pokemon = pokemon,
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
