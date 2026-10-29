package com.duoc.AgroDuocApp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.AgroDuocApp.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    var usuario by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    // Valida campos y revisa el login en la base de datos
    fun validarYLogin(onSuccess: () -> Unit) {
        if (usuario.isBlank()) {
            errorMessage = "El campo usuario no puede estar vacio"
            return
        }

        if (password.isEmpty() || password.length < 4) {
            errorMessage = "La contraseña debe tener al menos 4 caracteres"
            return
        }

        viewModelScope.launch {
            val user = repository.validarCredenciales(usuario, password)
            if (user != null) {
                repository.guardarSesionActiva(user.rut)
                errorMessage = null
                onSuccess()
            } else {
                errorMessage = "Usuario o contraseña incorrectos"
            }
        }
    }
}
