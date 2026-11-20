package com.duoc.AgroDuocApp.remote

import com.duoc.AgroDuocApp.model.AuthResponse
import com.duoc.AgroDuocApp.model.OfertaDto
import com.duoc.AgroDuocApp.model.SolicitudDto
import com.duoc.AgroDuocApp.model.UsuarioDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface XanoApiService {
    // 1. Obtener lista de usuarios
    @GET("usuario")
    suspend fun getUsuarios(): List<UsuarioDto>

    // 2. Obtener lista de solicitudes de productores
    @GET("solicitud")
    suspend fun getSolicitudes(): List<SolicitudDto>

    // 3. Crear una nueva solicitud (POST)
    @POST("solicitud")
    suspend fun crearSolicitud(@Body solicitud: SolicitudDto): SolicitudDto

    // 4. Obtener lista de ofertas de aplicadores
    @GET("oferta")
    suspend fun getOfertas(): List<OfertaDto>

    // 5. Crear una nueva oferta (POST)
    @POST("oferta")
    suspend fun crearOferta(@Body oferta: OfertaDto): OfertaDto

    // 6. Login de usuario
    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): AuthResponse

    // 7. Registro de usuario con JSON limpio
    @POST("auth/signup")
    suspend fun signup(@Body body: Map<String, String>): AuthResponse
}
