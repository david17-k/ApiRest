package com.example.apirest.service


import com.example.apirest.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemon(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonResponse
}
