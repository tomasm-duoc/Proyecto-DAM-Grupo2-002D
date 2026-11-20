package com.duoc.AgroDuocApp.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.AgroDuocApp.remote.RetrofitClient
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    var selectedRole by mutableStateOf("Productor") // "productor" o "aplicador"
    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var rut by mutableStateOf("")
    var email by mutableStateOf("")
    var telefono by mutableStateOf("")
    var password by mutableStateOf("")
    var fotoPerfilUri by mutableStateOf<String?>(null)
    var capturedBitmap: Bitmap? = null // Almacena el bitmap de la cámara
    var errorMessage by mutableStateOf<String?>(null)

    // Convierte un Bitmap a una cadena Base64
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Valida el formulario y realiza el registro contra la API de Xano enviando la foto en Base64
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
            try {
                val api = RetrofitClient.getInstance(getApplication())

                // Convertir la foto capturada a Base64 (si existe)
                val base64Image = capturedBitmap?.let { bitmapToBase64(it) } ?: ""

                val body = mapOf(
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "rut" to rut,
                    "email" to email,
                    "telefono" to telefono,
                    "password" to password,
                    "rol" to selectedRole.lowercase(),
                    "foto_perfil" to base64Image
                )

                // Petición POST JSON a Xano auth/signup
                val response = api.signup(body)

                if (!response.authToken.isNullOrEmpty()) {
                    // Guardar el token Bearer en SharedPreferences
                    val prefs = getApplication<Application>().getSharedPreferences("TecMundoPrefs", Context.MODE_PRIVATE)
                    prefs.edit().apply {
                        putString("xano_auth_token", response.authToken)
                        putString("logged_user_email", email)
                        apply()
                    }

                    errorMessage = null
                    onSuccess()
                } else {
                    errorMessage = "No se pudo completar el registro en el servidor"
                }
            } catch (e: Exception) {
                val errorMsg = e.localizedMessage ?: ""
                errorMessage = if (errorMsg.contains("403") || errorMsg.contains("400") || errorMsg.contains("422")) {
                    "Este correo electrónico o RUT ya se encuentra registrado en el sistema, o prueba con otro."
                } else {
                    "Error al conectar con Xano: $errorMsg"
                }
            }
        }
    }
}
