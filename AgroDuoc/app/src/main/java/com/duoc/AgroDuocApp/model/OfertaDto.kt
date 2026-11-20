package com.duoc.AgroDuocApp.model

data class OfertaDto(
    val id: Int,
    val created_at: Long,
    val solicitud_id: Int,
    val aplicador_id: Int,
    val precio_por_ha: Int,
    val precio_total_iva: Int,
    val estado: String,
    val calificacion_otorgada: Int?
)
