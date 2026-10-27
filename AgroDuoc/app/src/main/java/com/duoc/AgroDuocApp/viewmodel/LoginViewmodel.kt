package com.duoc.AgroDuocApp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.duoc.AgroDuocApp.repository.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    var usuario by mutableStateOf("") // Representa el Nombre del usuario
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    fun validarLogin(): Boolean {
        if (usuario.isBlank()) {
            errorMessage = "El campo nombre no puede estar vacío"
            return false
        }

        if (password.isEmpty()) {
            errorMessage = "La contraseña no puede estar vacía"
            return false
        }

        if (password.length < 4) {
            errorMessage = "La contraseña debe tener al menos 4 caracteres"
            return false
        }

        val esValido = repository.validarCredenciales(usuario, password)
        if (esValido) {
            repository.guardarSesionActiva(usuario)
            errorMessage = null
            return true
        } else {
            errorMessage = "Nombre o contraseña incorrectos"
            return false
        }
    }
}