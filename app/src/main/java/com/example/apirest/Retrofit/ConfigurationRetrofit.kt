package com.example.apirest.Retrofit

import com.example.apirest.service.PokemonApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofit = Retrofit.Builder()
    .baseUrl("https://pokeapi.co/api/v2/") // Aquí va la URL base de la API
    .addConverterFactory(GsonConverterFactory.create()) // Usamos Gson para la serialización
    .build()
val apiService = retrofit.create(PokemonApi::class.java)
