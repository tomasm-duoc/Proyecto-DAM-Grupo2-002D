package com.duoc.AgroDuocApp.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.AgroDuocApp.model.UsuarioDto
import com.duoc.AgroDuocApp.remote.RetrofitClient
import com.duoc.AgroDuocApp.repository.UserRepository
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)

    var usuarioDto by mutableStateOf<UsuarioDto?>(null)
        private set
    var usuarioId by mutableStateOf<Int?>(null)
        private set
    var cargando by mutableStateOf(true)
        private set

    init {
        cargarPerfil()
    }

    // Carga los datos del usuario activo desde Xano
    fun cargarPerfil() {
        viewModelScope.launch {
            cargando = true
            val context = getApplication<Application>()
            val prefs = context.getSharedPreferences("TecMundoPrefs", Context.MODE_PRIVATE)
            val emailActivo = prefs.getString("logged_user_email", null)

            if (!emailActivo.isNullOrBlank()) {
                try {
                    val apiService = RetrofitClient.getInstance(context)
                    val usuariosXano = apiService.getUsuarios()

                    val encontrado = usuariosXano.find {
                        it.email.equals(emailActivo.trim(), ignoreCase = true)
                    }

                    if (encontrado != null) {
                        usuarioId = encontrado.id
                        usuarioDto = encontrado
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            cargando = false
        }
    }

    // Elimina la cuenta en Xano y limpia la sesión local
    fun eliminarCuenta(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (usuarioId != null) {
                    val context = getApplication<Application>()
                    val apiService = RetrofitClient.getInstance(context)
                    apiService.eliminarUsuario(usuarioId!!)
                }
                val context = getApplication<Application>()
                val prefs = context.getSharedPreferences("TecMundoPrefs", Context.MODE_PRIVATE)
                prefs.edit().clear().apply()

                onSuccess()
            } catch (e: Exception) {
                onError("Error al eliminar: ${e.localizedMessage}")
            }
        }
    }
}
