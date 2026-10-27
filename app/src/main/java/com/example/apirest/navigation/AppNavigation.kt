package com.example.apirest.navigation

import MainRoutes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apirest.features.pokemon.PokemonScreen


@Composable
fun AppNavifation(

){
    val navController= rememberNavController()

    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        NavHost(
            navController= navController,
            startDestination = MainRoutes.Pokemon
        ){
            composable<MainRoutes.Pokemon>{
                PokemonScreen(

                )
            }

        }
    }

}