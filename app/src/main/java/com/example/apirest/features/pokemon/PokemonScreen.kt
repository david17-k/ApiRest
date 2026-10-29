package com.example.apirest.features.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.example.apirest.R
import com.example.apirest.core.util.RequestResult
import com.example.apirest.model.Pokemon

// Tipos que se pueden filtrar (el nombre es el que usa la API)
val pokemonTypes = listOf("fire", "water", "grass", "electric", "psychic", "dragon")

// Generaciones que se pueden filtrar
val pokemonGenerations = listOf(1, 2, 3, 4, 5)


@Composable
fun PokemonScreen(
    onPokemonClick: (String) -> Unit = {},
    viewModel: PokemonViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    val pokemonList = viewModel.pokemonList.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
            .statusBarsPadding()
    ) {

        Text(
            text = if (isOnline) "Online" else "Offline",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isOnline)
                        Color(0xFF2E7D32)
                    else
                        Color(0xFFC62828)
                )
                .padding(4.dp)
        )

        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        )

        Text(
            text = "Tipo",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                FilterChip(
                    selected = state.selectedType == null &&
                            state.selectedGeneration == null,
                    onClick = {
                        viewModel.loadPokemon()
                    },
                    label = {
                        Text("Todos")
                    }
                )
            }

            items(pokemonTypes) { type ->
                FilterChip(
                    selected = state.selectedType == type,
                    onClick = { viewModel.filterByType(type) },
                    label = { Text(type.replaceFirstChar { it.uppercase() }) }
                )
            }
        }


        Text(
            text = "Generación",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(pokemonGenerations) { generation ->
                FilterChip(
                    selected = state.selectedGeneration == generation,
                    onClick = {
                        viewModel.filterByGeneration(generation)
                    },
                    label = {
                        Text("Gen $generation")
                    }
                )
            }
        }
        when {
            // 1. Mostrar el indicador mientras se consulta un filtro
            state.isFiltering -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // 2. Mostrar errores de los filtros
            state.filterError != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.filterError ?: "Error al filtrar",
                        color = MaterialTheme.colorScheme.error
                    )

                    Button(
                        onClick = { viewModel.retry() }
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            // 3. Mostrar los Pokémon filtrados
            state.selectedType != null ||
                    state.selectedGeneration != null -> {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.filteredPokemon.orEmpty()) { pokemon ->
                        PokemonItem(
                            pokemon = pokemon,
                            onClick = {
                                onPokemonClick(pokemon.name)
                            }
                        )
                    }

                    if (state.filteredPokemon?.isEmpty() == true) {
                        item {
                            Text(
                                text = "No se encontraron Pokémon.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // 4. Si no hay filtro, conservar la lista paginada original
            else -> {
                when (val refreshState = pokemonList.loadState.refresh) {
                    is LoadState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is LoadState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = refreshState.error.localizedMessage
                                    ?: "Error al cargar Pokémon",
                                color = MaterialTheme.colorScheme.error
                            )

                            Button(
                                onClick = { pokemonList.retry() }
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }

                    is LoadState.NotLoading -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(pokemonList.itemCount) { index ->
                                pokemonList[index]?.let { pokemon ->
                                    PokemonItem(
                                        pokemon = pokemon,
                                        onClick = {
                                            onPokemonClick(pokemon.name)
                                        }
                                    )
                                }
                            }

                            item {
                                when (pokemonList.loadState.append) {
                                    is LoadState.Loading -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }

                                    is LoadState.Error -> {
                                        Button(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            onClick = {
                                                pokemonList.retry()
                                            }
                                        ) {
                                            Text("Reintentar")
                                        }
                                    }

                                    else -> Unit
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Tarjeta de un Pokémon en la lista
@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.photoUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = pokemon.name.replaceFirstChar {
                    it.uppercase()
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}