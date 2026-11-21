package com.duoc.AgroDuocApp.ui.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing_screen")
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Home : Screen("home_screen")
    object Perfil : Screen("Perfil_screen")
    object CrearSolicitud : Screen("crear_solicitud_screen")
    object Faqs : Screen("faqs")
    object DetalleSolicitudProductor : Screen("detalle_solicitud_productor/{solicitudId}"){
        fun createRoute(solicitudId: Int) = "detalle_solicitud_productor/$solicitudId"
    }
}