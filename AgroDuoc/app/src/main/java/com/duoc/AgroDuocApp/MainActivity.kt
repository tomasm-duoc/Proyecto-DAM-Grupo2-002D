package com.duoc.AgroDuocApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.duoc.AgroDuocApp.repository.UserRepository
import com.duoc.AgroDuocApp.ui.navigation.AppNavigation
import com.duoc.AgroDuocApp.ui.navigation.Screen
import com.duoc.AgroDuocApp.ui.theme.AgroDuocTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userRepository = UserRepository(applicationContext)
        val sesionActiva = userRepository.obtenerSesionActiva()

        // Si hay una sesión activa redirige a Home; de lo contrario, muestra la LandingScreen.
        val startDestination = if (sesionActiva != null) {
            Screen.Home.route
        } else {
            Screen.Landing.route
        }

        setContent {
            AgroDuocTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(startDestination = startDestination)
                }
            }
        }
    }
}