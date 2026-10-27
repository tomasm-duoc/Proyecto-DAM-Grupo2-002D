package com.duoc.AgroDuocApp.repository

import android.content.Context
import android.content.SharedPreferences
import com.duoc.AgroDuocApp.model.User

class UserRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("TecMundoPrefs", Context.MODE_PRIVATE)

    fun registrarUsuario(user: User, password: String): Boolean {
        // Verificamos si ya existe una cuenta con este nombre
        if (prefs.contains("user_${user.nombre}_pass")) {
            return false // Ya existe
        }

        // Guardamos las credenciales utilizando el Nombre
        prefs.edit().apply {
            putString("user_${user.nombre}_pass", password)
            putString("user_${user.nombre}_nombre", user.nombre)
            apply()
        }
        return true
    }

    fun validarCredenciales(nombre: String, pass: String): Boolean {
        val savedPass = prefs.getString("user_${nombre}_pass", null)
        return savedPass != null && savedPass == pass
    }

    // --- Control de Sesión Activa ---
    fun guardarSesionActiva(nombre: String) {
        prefs.edit().putString("logged_user_name", nombre).apply()
    }

    fun obtenerSesionActiva(): String? {
        return prefs.getString("logged_user_name", null)
    }

    fun cerrarSesion() {
        prefs.edit().remove("logged_user_name").apply()
    }
}