package com.duoc.AgroDuocApp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.AgroDuocApp.R
import com.duoc.AgroDuocApp.ui.CrearOfertaScreen
import com.duoc.AgroDuocApp.ui.CrearSolicitudScreen
import com.duoc.AgroDuocApp.ui.DetalleSolicitudProductorScreen
import com.duoc.AgroDuocApp.ui.FaqsScreen
import com.duoc.AgroDuocApp.ui.HomeScreen
import com.duoc.AgroDuocApp.ui.LandingScreen
import com.duoc.AgroDuocApp.ui.LoginScreen
import com.duoc.AgroDuocApp.ui.OfertasScreen
import com.duoc.AgroDuocApp.ui.PerfilScreen
import com.duoc.AgroDuocApp.ui.RegisterScreen
import com.duoc.AgroDuocApp.ui.SolicitudesScreen

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
                },
                onNavigateToPerfil = {
                    navController.navigate(Screen.Perfil.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToInfo = {
                    navController.navigate(Screen.Faqs.route) {
                        launchSingleTop = true
                    }
                },
                onIrASolicitudes = {
                    navController.navigate(Screen.Solicitudes.route) {
                        launchSingleTop = true
                    }
                },
                onIrAOfertas = {
                    navController.navigate(Screen.Ofertas.route){
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Ofertas.route) {
            OfertasScreen(
                onLogout = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToPerfil = {
                    navController.navigate(Screen.Perfil.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToInfo = {
                    navController.navigate(Screen.Faqs.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToCrearOferta = {
                    navController.navigate(Screen.CrearOferta.route) { // <--- Irá a la pantalla para crear oferta
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.CrearOferta.route) {
            CrearOfertaScreen(
                onBackClick = {
                    navController.popBackStack() // <--- Vuelve atrás sin duplicar pantallas
                },
                onOfertaCreada = {
                    navController.popBackStack() // <--- Regresa a OfertasScreen y recarga la lista desde Xano
                }
            )
        }

        composable(Screen.Solicitudes.route) {
            SolicitudesScreen(
                onLogout = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToPerfil = {
                    navController.navigate(Screen.Perfil.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToInfo = {
                    navController.navigate(Screen.Faqs.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToCrearSolicitud = {
                    navController.navigate(Screen.CrearSolicitud.route)
                },
                onSolicitudClick = { solicitudId ->
                    navController.navigate(Screen.DetalleSolicitudProductor.createRoute(solicitudId))
                }
            )
        }

        composable(Screen.CrearSolicitud.route) {
            CrearSolicitudScreen(
                onSolicitudGuardada = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Perfil.route) {
            PerfilScreen(
                onLogout = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToInfo = {
                    navController.navigate(Screen.Faqs.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Faqs.route){
            FaqsScreen(
                onBackClick = {
                    if(navController.currentDestination?.route == Screen.Faqs.route){
                        val volvio = navController.popBackStack()
                        if(!volvio){
                            navController.navigate(Screen.Home.route){
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.DetalleSolicitudProductor.route,
            arguments = listOf(navArgument("solicitudId"){ type = NavType.IntType })
        ){ backStackEntry ->
            val solicitudId = backStackEntry.arguments?.getInt("solicitudId") ?: 0
            DetalleSolicitudProductorScreen(
                solicitudId = solicitudId,
                onBackClick = {
                    if(navController.currentDestination?.route == Screen.DetalleSolicitudProductor.route){
                        val volvio = navController.popBackStack()
                        if(!volvio){
                            navController.navigate(Screen.Home.route){
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
    }
}