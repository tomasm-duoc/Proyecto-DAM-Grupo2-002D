package com.duoc.AgroDuocApp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var usuario by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    fun validarLogin(): Boolean {
        return when {
            usuario.isBlank() -> {
                errorMessage = "El usuario no puede estar vacío"
                false
            }
            password.isBlank() -> {
                errorMessage = "La contraseña no puede estar vacía"
                false
            }
            password.length < 4 -> {
                errorMessage = "La contraseña debe tener al menos 4 caracteres"
                false
            }
            else -> {
                errorMessage = null
                true
            }
        }
    }
}