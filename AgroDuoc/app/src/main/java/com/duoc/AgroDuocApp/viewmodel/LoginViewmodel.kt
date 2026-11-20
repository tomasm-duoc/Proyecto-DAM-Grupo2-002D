package com.duoc.AgroDuocApp.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.AgroDuocApp.remote.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var usuario by mutableStateOf("") // Email
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    // Realiza el inicio de sesión contra la API de Xano
    fun validarYLogin(onSuccess: () -> Unit) {
        if (usuario.isBlank()) {
            errorMessage = "El campo de correo no puede estar vacio"
            return
        }

        if (password.isEmpty() || password.length < 4) {
            errorMessage = "La contraseña debe tener al menos 4 caracteres"
            return
        }

        viewModelScope.launch {
            try {
                val api = RetrofitClient.getInstance(getApplication())
                val body = mapOf("email" to usuario, "password" to password)
                val response = api.login(body)

                if (!response.authToken.isNullOrEmpty()) {
                    // Guardar el token Bearer en SharedPreferences para autenticar peticiones futuras
                    val prefs = getApplication<Application>().getSharedPreferences("TecMundoPrefs", Context.MODE_PRIVATE)
                    prefs.edit().apply {
                        putString("xano_auth_token", response.authToken)
                        putString("logged_user_email", usuario)
                        apply()
                    }

                    errorMessage = null
                    onSuccess()
                } else {
                    errorMessage = "Correo o contraseña incorrectos"
                }
            } catch (e: Exception) {
                val errorMsg = e.localizedMessage ?: ""
                errorMessage = if (errorMsg.contains("401") || errorMsg.contains("400") || errorMsg.contains("422")) {
                    "Credenciales invalidas o usuario no registrado."
                } else {
                    "Error al conectar con Xano: $errorMsg"
                }
            }
        }
    }
}
