package com.example.apirest.service


import com.example.apirest.model.GenerationResponse
import com.example.apirest.model.PokemonDetailDto
import com.example.apirest.model.PokemonResponse
import com.example.apirest.model.TypeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokemonApi {

    // Lista de Pokémon
    @GET("pokemon")
    suspend fun getPokemon(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonResponse

    // Detalle de un Pokémon (por id o nombre)
    @GET("pokemon/{idOrName}")
    suspend fun getPokemonDetail(
        @Path("idOrName") idOrName: String
    ): PokemonDetailDto

    // Pokémon de un tipo (Ej: fire, water)
    @GET("type/{idOrName}")
    suspend fun getPokemonByType(
        @Path("idOrName") idOrName: String
    ): TypeResponse

    // Pokémon de una generación (Ej: 1, 2, 3)
    @GET("generation/{id}")
    suspend fun getPokemonByGeneration(
        @Path("id") id: Int
    ): GenerationResponse
}
