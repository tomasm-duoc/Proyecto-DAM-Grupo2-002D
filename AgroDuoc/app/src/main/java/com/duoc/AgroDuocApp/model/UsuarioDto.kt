package com.duoc.AgroDuocApp.model

data class UsuarioDto(
    val id: Int,
    val created_at: Long,
    val nombre: String,
    val apellido: String,
    val rut: String,
    val email: String,
    val telefono: String,
    val rol: String,
    val foto_perfil: XanoImage?,
    val calificacion: Double
)
