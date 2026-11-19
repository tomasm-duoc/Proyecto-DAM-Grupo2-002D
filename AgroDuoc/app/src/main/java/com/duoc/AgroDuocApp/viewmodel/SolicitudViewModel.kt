package com.duoc.AgroDuocApp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.AgroDuocApp.model.Solicitud
import com.duoc.AgroDuocApp.repository.AppDatabase
import com.duoc.AgroDuocApp.repository.SolicitudRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SolicitudViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SolicitudRepository = SolicitudRepository(
        AppDatabase.getDatabase(application).solicitudDao()
    )

    val solicitudes: StateFlow<List<Solicitud>> = repository.todasLasSolicitudes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var ubicacion by mutableStateOf("")
    var tipoCultivo by mutableStateOf("")
    var tipoProducto by mutableStateOf("")
    var superficie by mutableStateOf("")
    var tieneAgua by mutableStateOf(true)
    var aplicacion by mutableStateOf("")
    var fechaDisponibilidad by mutableStateOf("")
    var comentarios by mutableStateOf("")
    var fotoUri by mutableStateOf<String?>(null)

    var obstaculosSeleccionados by mutableStateOf(setOf<String>())
        private set

    var guardadoExitoso by mutableStateOf(false)
        private set

    fun toggleObstaculo(obstaculo: String){
        obstaculosSeleccionados = if(obstaculosSeleccionados.contains(obstaculo)){
            obstaculosSeleccionados - obstaculo
        } else {
            obstaculosSeleccionados + obstaculo
        }
    }

    fun guardarSolicitud(){
        val nuevaSolicitud = Solicitud(
            ubicacion = ubicacion.trim(),
            tipoCultivo = tipoCultivo.trim(),
            tipoProducto = tipoProducto.trim(),
            superficie = superficie.trim(),
            tieneAgua = tieneAgua,
            aplicacion = aplicacion,
            fechaDisponibilidad = fechaDisponibilidad.trim(),
            obstaculos = obstaculosSeleccionados.joinToString(", "),
            comentarios = comentarios.trim(),
            fotoUri = fotoUri
        )

        viewModelScope.launch {
            repository.guardarSolicitud(nuevaSolicitud)
            limpiarFormulario()
            guardadoExitoso = true
        }
    }

    fun limpiarFormulario(){
        ubicacion = ""
        tipoCultivo = ""
        tipoProducto = ""
        superficie = ""
        tieneAgua = true
        aplicacion = "Diurna"
        fechaDisponibilidad = ""
        obstaculosSeleccionados = emptySet<String>()
        comentarios = ""
        fotoUri = null
    }

    fun quitarEstadoGuardado(){
        guardadoExitoso = false
    }
}