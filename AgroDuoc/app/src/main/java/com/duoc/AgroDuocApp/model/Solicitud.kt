package com.duoc.AgroDuocApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes")
data class Solicitud(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,    // Se puede cambiar de tipo
    val ubicacion: String,
    val tipoCultivo: String,
    val tipoProducto: String,
    val superficie: String,
    val tieneAgua: Boolean,
    val aplicacion: String,
    val fechaDisponibilidad: String,
    val obstaculos: String,
    val comentarios: String,
    val fotoUri: String? = null     // Para agregar foto en la solicitud
) {
}