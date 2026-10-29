package com.example.apirest.data.remote

import com.example.apirest.model.Pokemon
import com.google.gson.annotations.SerializedName
import kotlin.toString

// ---------- GET pokemon?offset={offset}&limit={limit} ----------

data class PokemonResponse(
    val count: Int,
    val results: List<PokemonDto>
)

data class PokemonDto(
    val name: String,
    val url: String,

) {

    val id: Int
        get() = url.trimEnd('/')
            .substringAfterLast('/')
            .toIntOrNull() ?: 0

    val photoUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"


    fun toDomain(): Pokemon = Pokemon(
        id = id,
        name = name,
        photoUrl=photoUrl
    )
}

// ---------- GET pokemon/{idOrName} ----------

data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeSlot>
)

data class PokemonTypeSlot(
    val type: PokemonType
)

data class PokemonType(
    val name: String
)

// ---------- GET type/{idOrName} ----------

data class TypeResponse(
    val pokemon: List<TypePokemonSlot>
)

data class TypePokemonSlot(
    val pokemon: PokemonDto
)

// ---------- GET generation/{id} ----------

data class GenerationResponse(
    // En el JSON el campo se llama "pokemon_species"
    @SerializedName("pokemon_species")
    val pokemonSpecies: List<PokemonDto>
)


