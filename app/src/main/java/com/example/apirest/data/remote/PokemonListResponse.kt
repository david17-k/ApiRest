package com.example.apirest.model

data class PokemonResponse(
    val count: Int,
    val results: List<PokemonDto>
)

data class PokemonDto(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.trimEnd('/')
            .substringAfterLast('/')
            .toIntOrNull() ?: 0

    val photoUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}
