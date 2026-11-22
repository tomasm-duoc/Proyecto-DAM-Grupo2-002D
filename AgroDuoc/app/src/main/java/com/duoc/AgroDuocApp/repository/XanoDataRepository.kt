package com.duoc.AgroDuocApp.repository

import android.content.Context
import com.duoc.AgroDuocApp.model.SolicitudDto
import com.duoc.AgroDuocApp.model.UsuarioDto
import com.duoc.AgroDuocApp.remote.RetrofitClient
import com.duoc.AgroDuocApp.remote.XanoApiService

class XanoDataRepository(
    context: Context,
    private val apiService: XanoApiService = RetrofitClient.getInstance(context),
    private val userRepository: UserRepository = UserRepository(context)
) {

    suspend fun obtenerUsuarioActual(): UsuarioDto? {   // <--- Obtiene el UsuarioDto del usuario logeado
        val sesionActiva = userRepository.obtenerSesionActiva()?.trim() ?: return null
        val usuarios = apiService.getUsuarios()
        return usuarios.find {
            it.rut.equals(sesionActiva, ignoreCase = true) ||
                    it.email.equals(sesionActiva, ignoreCase = true)
        }
    }

    suspend fun obtenerSolicitudesDeProductorActual(): Pair<UsuarioDto?, List<SolicitudDto>> {  // <--- Obtiene las Solicitudes segun el productor que este logeado
        val usuarioActual = obtenerUsuarioActual() ?: return Pair(null, emptyList())
        val todasLasSolicitudes = apiService.getSolicitudes()
        val propias = todasLasSolicitudes.filter { it.productor_id == usuarioActual.id }
        return Pair(usuarioActual, propias)
    }

    suspend fun crearSolicitudXano(   // <--- Crea las solicitudes en Xano
        superficieHa: Double,
        tipoProducto: String,
        tieneAgua: Boolean,
        tipoAplicacion: String,
        fotos: List<String>?,
        comentarios: String?,
        latitud: Double? = null,
        longitud: Double? = null
    ): SolicitudDto {
        val usuarioActual = obtenerUsuarioActual()
            ?: throw IllegalStateException("No se encontró una sesión de usuario activa en Xano")

        val nuevaSolicitud = SolicitudDto(
            id = 0,
            created_at = System.currentTimeMillis(),
            productor_id = usuarioActual.id,
            superficie_ha = superficieHa,
            tipo_producto = tipoProducto.trim().lowercase(),
            tiene_agua = tieneAgua,
            tipo_aplicacion = tipoAplicacion.trim().lowercase(),
            fotos = fotos,
            comentarios = comentarios,
            latitud = latitud,
            longitud = longitud,
            estado = "pendiente"
        )

        return apiService.crearSolicitud(nuevaSolicitud)
    }
}