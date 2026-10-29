package com.duoc.AgroDuocApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val rut: String, // El RUT se utiliza como clave primaria única
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val rol: String, // "Productor" o "Aplicador"
    val password: String = "",
    val fotoPerfilUri: String? = null // Guarda la URI/ruta de la foto capturada con la Cámara
)