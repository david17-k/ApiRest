package com.example.apirest.Retrofit

import com.example.apirest.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofit = Retrofit.Builder()
    .baseUrl("https://pokeapi.co/api/v2/pokemon/ditto") // Aquí va la URL base de la API
    .addConverterFactory(GsonConverterFactory.create()) // Usamos Gson para la serialización
    .build()
val apiService = retrofit.create(ApiService::class.java)
