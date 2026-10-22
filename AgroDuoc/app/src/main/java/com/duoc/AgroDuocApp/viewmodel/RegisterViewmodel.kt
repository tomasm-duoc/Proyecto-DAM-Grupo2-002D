package com.duoc.AgroDuocApp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.duoc.AgroDuocApp.model.User

class RegisterViewModel : ViewModel() {
    var selectedRole by mutableStateOf("Productor")
    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var rut by mutableStateOf("")
    var email by mutableStateOf("")
    var telefono by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    fun validarRegistro(): Boolean {
        return when {
            nombre.isBlank() || apellido.isBlank() -> {
                errorMessage = "Por favor completa tu nombre y apellido"
                false
            }
            rut.isBlank() || !rut.contains("-") -> {
                errorMessage = "Ingresa un RUT válido (ej: 12345678-9)"
                false
            }
            email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                errorMessage = "Ingresa un correo electrónico válido"
                false
            }
            telefono.isBlank() -> {
                errorMessage = "El teléfono de contacto es obligatorio"
                false
            }
            else -> {
                errorMessage = null
                true
            }
        }
    }

    fun obtenerUsuario(): User {
        return User(
            nombre = nombre,
            apellido = apellido,
            rut = rut,
            email = email,
            telefono = telefono,
            rol = selectedRole
        )
    }
}