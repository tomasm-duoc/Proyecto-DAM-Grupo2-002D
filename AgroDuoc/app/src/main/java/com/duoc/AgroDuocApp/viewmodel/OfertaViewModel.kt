package com.duoc.AgroDuocApp.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.AgroDuocApp.model.OfertaDto
import com.duoc.AgroDuocApp.model.SolicitudDto
import com.duoc.AgroDuocApp.remote.RetrofitClient
import com.duoc.AgroDuocApp.repository.XanoDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OfertaViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.getInstance(application) // <--- Cliente Retrofit hacia Xano
    private val xanoRepository = XanoDataRepository(application) // <--- Para obtener el usuario actual si existe

    private val _ofertas = MutableStateFlow<List<OfertaDto>>(emptyList()) // <--- Guarda las ofertas traídas desde Xano
    val ofertas: StateFlow<List<OfertaDto>> = _ofertas.asStateFlow() // <--- Expone las ofertas a la UI

    private val _solicitudesDisponibles = MutableStateFlow<List<SolicitudDto>>(emptyList()) // <--- Guarda solicitudes para el selector
    val solicitudesDisponibles: StateFlow<List<SolicitudDto>> = _solicitudesDisponibles.asStateFlow()

    private val _cargando = MutableStateFlow(false) // <--- Controla el spinner de carga
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null) // <--- Guarda errores de red o validación
    val error: StateFlow<String?> = _error.asStateFlow()

    // Carga todas las ofertas desde Xano (@GET("oferta"))
    fun cargarOfertas() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                _ofertas.value = apiService.getOfertas() // <--- Llama al endpoint GET /oferta[cite: 2]
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar ofertas desde Xano"
            } finally {
                _cargando.value = false
            }
        }
    }

    // Carga las solicitudes disponibles desde Xano para elegir una al crear oferta
    fun cargarSolicitudesParaOfertar() {
        viewModelScope.launch {
            try {
                _solicitudesDisponibles.value = apiService.getSolicitudes() // <--- Llama al endpoint GET /solicitud[cite: 2]
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar solicitudes"
            }
        }
    }

    // Crea y guarda una nueva oferta en Xano (@POST("oferta"))
    fun crearOferta(
        solicitudId: Int,
        precioPorHa: Int,
        precioTotalIva: Int,
        onExito: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                // Toma el ID del usuario logueado o usa 1 como respaldo si no hay sesión
                val aplicadorId = try {
                    xanoRepository.obtenerUsuarioActual()?.id ?: 1
                } catch (_: Exception) {
                    1
                }

                val nuevaOferta = OfertaDto(
                    id = 0,
                    created_at = System.currentTimeMillis(),
                    solicitud_id = solicitudId,
                    aplicador_id = aplicadorId,
                    precio_por_ha = precioPorHa,
                    precio_total_iva = precioTotalIva,
                    estado = "pendiente", // <--- En minúsculas por validación case-sensitive de Xano
                    calificacion_otorgada = 5
                )

                apiService.crearOferta(nuevaOferta) // <--- Envía el POST /oferta a Xano[cite: 2]
                _ofertas.value = apiService.getOfertas() // <--- Refresca la lista con la nueva oferta
                onExito()
            } catch (e: Exception) {
                _error.value = e.message ?: "No se pudo guardar la oferta"
            } finally {
                _cargando.value = false
            }
        }
    }
}