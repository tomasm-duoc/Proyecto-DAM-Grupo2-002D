package com.duoc.AgroDuocApp.model

data class User(
    val nombre: String,
    val apellido: String,
    val rut: String,
    val email: String,
    val telefono: String,
    val rol: String // "Productor" o "Aplicador"
)