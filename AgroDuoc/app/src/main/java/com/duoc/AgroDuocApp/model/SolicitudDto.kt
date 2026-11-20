package com.duoc.AgroDuocApp.model

data class SolicitudDto(
    val id: Int,
    val created_at: Long,
    val productor_id: Int,
    val superficie_ha: Double,
    val tipo_producto: String,
    val tiene_agua: Boolean,
    val tipo_aplicacion: String,
    val obstaculos: List<String>?,
    val fotos: List<XanoImage>?,
    val comentarios: String?,
    val latitud: Double?,
    val longitud: Double?,
    val estado: String
)
