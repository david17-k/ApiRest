package com.example.apirest.core.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Avisa si el celular tiene o no conexión a internet
@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext context: Context
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // true = online, false = offline
    private val _isOnline = MutableStateFlow(connectivityManager.activeNetwork != null)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        // Android llama a estas funciones cada vez que cambia la conexión
        connectivityManager.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    _isOnline.value = true
                }

                override fun onLost(network: Network) {
                    _isOnline.value = false
                }
            }
        )
    }
}
