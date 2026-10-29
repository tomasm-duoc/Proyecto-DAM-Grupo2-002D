package com.duoc.AgroDuocApp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.AgroDuocApp.model.User
import com.duoc.AgroDuocApp.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    var selectedRole by mutableStateOf("Productor")
    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var rut by mutableStateOf("")
    var email by mutableStateOf("")
    var telefono by mutableStateOf("")
    var password by mutableStateOf("")
    var fotoPerfilUri by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    // Valida el formulario de registro y lo guarda en Room
    fun validarYRegistrar(onSuccess: () -> Unit) {
        val esValido = when {
            nombre.isBlank() || apellido.isBlank() -> {
                errorMessage = "Completa tu nombre y apellido"
                false
            }
            rut.isBlank() || !rut.contains("-") -> {
                errorMessage = "Ingresa un RUT valido (ej: 12345678-9)"
                false
            }
            email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                errorMessage = "Ingresa un correo valido"
                false
            }
            telefono.isBlank() -> {
                errorMessage = "El telefono es obligatorio"
                false
            }
            password.isBlank() || password.length < 4 -> {
                errorMessage = "La contraseña debe tener al menos 4 caracteres"
                false
            }
            else -> {
                errorMessage = null
                true
            }
        }

        if (!esValido) return

        viewModelScope.launch {
            val nuevoUsuario = User(
                rut = rut,
                nombre = nombre,
                apellido = apellido,
                email = email,
                telefono = telefono,
                rol = selectedRole,
                password = password,
                fotoPerfilUri = fotoPerfilUri
            )

            val registradoConExito = repository.registrarUsuario(nuevoUsuario)

            if (registradoConExito) {
                errorMessage = null
                onSuccess()
            } else {
                errorMessage = "El RUT o correo ya esta registrado"
            }
        }
    }
}
