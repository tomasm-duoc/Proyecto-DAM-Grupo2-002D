package com.duoc.AgroDuocApp.model

data class Solicitud(
    val id: String,
    val productorRut: String,
    val ubicacion: String,
    val tipoCultivo: String,
    val tipoProducto: String,
    val superficie: String,
    val tieneAgua: Boolean,
    val aplicacion: String,
    val fechaDisponibilidad: String,
    val obstaculos: String,
    val comentarios: String,
    val fotoUri: String? = null
) {
}