package com.duoc.AgroDuocApp.model

data class Producto(
    val nombre: String = "Nombre Item",
    val precio: String = "Precio: XXXXXX",
    val nombreVendedor: String = "Nombre Vendedor",
    val calificacion: Float = 5.0f,
    //val imagenProductoRes: Int = R.drawable.ic_launcher_background, // Reemplazar por tu drawable/imagen
    //val imagenVendedorRes: Int = R.drawable.ic_launcher_background
)