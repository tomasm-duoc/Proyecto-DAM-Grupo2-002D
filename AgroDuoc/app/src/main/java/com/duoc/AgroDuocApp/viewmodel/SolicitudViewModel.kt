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
import com.duoc.AgroDuocApp.model.SolicitudDto
import com.duoc.AgroDuocApp.model.UsuarioDto
import com.duoc.AgroDuocApp.repository.XanoDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.util.Log
import retrofit2.HttpException

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

    private val xanoDataRepository = XanoDataRepository(application)

    private val _usuarioActual = MutableStateFlow<UsuarioDto?>(null)    // <--- Guardar al usuario y actualiza al usuario activo (comienza en null)
    val usuarioActual: StateFlow<UsuarioDto?> = _usuarioActual.asStateFlow()    // <--- Lo muestra solo para leer, para que la pantalla cambie

    private val _solicitudesProductorXano = MutableStateFlow<List<SolicitudDto>>(emptyList())
    val solicitudesProductorXano: StateFlow<List<SolicitudDto>> = _solicitudesProductorXano.asStateFlow()

    private val _cargandoXano = MutableStateFlow(false)
    val cargandoXano: StateFlow<Boolean> = _cargandoXano.asStateFlow()

    private val _errorXano = MutableStateFlow<String?>(null)
    val errorXano: StateFlow<String?> = _errorXano.asStateFlow()

    fun cargarDatosProductor() {
        viewModelScope.launch {
            _cargandoXano.value = true
            _errorXano.value = null
            try {   // <--- buscara al usuario de la cuenta actual en la base de xano y guardara los datos de el y sus solicitudes asociadas
                val (usuario, solicitudesPropias) = xanoDataRepository.obtenerSolicitudesDeProductorActual()
                _usuarioActual.value = usuario
                _solicitudesProductorXano.value = solicitudesPropias
            } catch (e: Exception) {    // <--- En el caso de que falle la red o Xano lanza estos errores para que crashe la app
                _errorXano.value = e.message ?: "Error al cargar datos desde Xano"
            } finally {
                _cargandoXano.value = false
            }
        }
    }

    fun guardarSolicitud() {
        viewModelScope.launch {     // <--- Convierte los datos que recibe
            try {
                val superficieDouble = superficie.trim().replace(",", ".").toDoubleOrNull() ?: 0.0  // Int -> Double / Formato: 0.0
                val listaFotos = fotoUri?.let { listOf(it) }
                val comentariosCompletos = buildString {    // <--- Toma cultivo, obstaculos y comentarios como un solo texto limpio
                    if (tipoCultivo.isNotBlank()) append("Cultivo: ${tipoCultivo.trim()}. ")
                    if (ubicacion.isNotBlank()) append("Ubicación: ${ubicacion.trim()}. ")
                    if (obstaculosSeleccionados.isNotEmpty()) {
                        append("Obstáculos: ${obstaculosSeleccionados.joinToString(", ")}. ")
                    }
                    if (comentarios.isNotBlank()) append(comentarios.trim())
                }.trim().ifBlank { null } // <--- Si en esos campos no hay nada devuelve un null

                xanoDataRepository.crearSolicitudXano(    // <--- Guarda la solicitud en xano con los datos convertidos antes
                    superficieHa = superficieDouble,
                    tipoProducto = tipoProducto.trim(),
                    tieneAgua = tieneAgua,
                    tipoAplicacion = aplicacion.ifBlank { "Diurna" },
                    fotos = listaFotos,
                    comentarios = comentariosCompletos
                )

                cargarDatosProductor() // Para cargar la lista de xano con la nueva solicitud
                limpiarFormulario() // Limpia el formulario
                guardadoExitoso = true  // Para tirar la notificacion al crear la solicitud
            } catch (e: HttpException) {    // <--- mismo control de errores que antes (En el caso de que falle la red o Xano lanza estos errores para que crashe la app)
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("XANO_POST", "HTTP ${e.code()}: $errorBody")
                _errorXano.value = "Error Xano (${e.code()}): $errorBody"
            } catch (e: Exception) {
                Log.e("XANO_POST", "Error al crear solicitud", e)
                _errorXano.value = e.message ?: "Error al crear la solicitud en Xano"
            } catch (e: Exception) {
                _errorXano.value = e.message ?: "Error al crear la solicitud en Xano"
            }
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