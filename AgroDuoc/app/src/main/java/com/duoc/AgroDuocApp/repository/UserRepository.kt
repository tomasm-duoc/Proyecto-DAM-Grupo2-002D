package com.duoc.AgroDuocApp.repository

import android.content.Context
import android.content.SharedPreferences
import com.duoc.AgroDuocApp.model.User

class UserRepository(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()
    private val prefs: SharedPreferences = context.getSharedPreferences("TecMundoPrefs", Context.MODE_PRIVATE)

    // Registra al usuario validando que no exista previamente
    suspend fun registrarUsuario(user: User): Boolean {
        val existingByRut = userDao.getUserByRut(user.rut)
        if (existingByRut != null) return false

        val existingByEmail = userDao.getUserByEmail(user.email)
        if (existingByEmail != null) return false

        userDao.insertUser(user)
        return true
    }

    // Valida credenciales contra Room
    suspend fun validarCredenciales(identifier: String, pass: String): User? {
        return userDao.loginUser(identifier, pass)
    }

    suspend fun getUserByRut(rut: String): User? {
        return userDao.getUserByRut(rut)
    }

    // Guarda el RUT para mantener la sesion iniciada
    fun guardarSesionActiva(rut: String) {
        prefs.edit().putString("logged_user_rut", rut).apply()
    }

    fun obtenerSesionActiva(): String? {
        return prefs.getString("logged_user_rut", null)
    }

    fun cerrarSesion() {
        prefs.edit().remove("logged_user_rut").apply()
    }
}
