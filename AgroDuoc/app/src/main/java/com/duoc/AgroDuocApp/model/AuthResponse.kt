package com.duoc.AgroDuocApp.model

data class AuthResponse(
    val authToken: String,
    val user: UsuarioDto?
)
