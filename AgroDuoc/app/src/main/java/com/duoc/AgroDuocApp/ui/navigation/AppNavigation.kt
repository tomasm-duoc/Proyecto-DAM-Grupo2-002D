package com.duoc.AgroDuocApp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.AgroDuocApp.R
import com.duoc.AgroDuocApp.ui.HomeScreen
import com.duoc.AgroDuocApp.ui.LandingScreen
import com.duoc.AgroDuocApp.ui.LoginScreen
import com.duoc.AgroDuocApp.ui.RegisterScreen

@Composable
fun AppNavigation(startDestination: String = Screen.Landing.route) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination // Arranca en LandingScreen si no hay sesión iniciada
    ) {
        composable(Screen.Landing.route) {
            LandingScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                backgroundImageRes = R.drawable.fondo_agricola
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}