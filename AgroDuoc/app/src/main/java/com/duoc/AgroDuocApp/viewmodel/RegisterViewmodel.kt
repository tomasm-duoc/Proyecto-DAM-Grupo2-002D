package com.duoc.AgroDuocApp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.duoc.AgroDuocApp.model.User
import com.duoc.AgroDuocApp.repository.UserRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    var selectedRole by mutableStateOf("Productor")
    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var rut by mutableStateOf("")
    var email by mutableStateOf("")
    var telefono by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    fun validarRegistro(): Boolean {
        val esValido = when {
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

        if (!esValido) return false

        val nuevoUsuario = obtenerUsuario()
        val registradoConExito = repository.registrarUsuario(nuevoUsuario, password)

        if (registradoConExito) {
            errorMessage = null
            return true
        } else {
            errorMessage = "El usuario o correo electrónico ya está registrado"
            return false
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