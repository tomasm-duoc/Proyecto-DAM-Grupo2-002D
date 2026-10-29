package com.duoc.AgroDuocApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String = "Nombre Item",
    val precio: String = "Precio: XXXXXX",
    val nombreVendedor: String = "Nombre Vendedor",
    val calificacion: Float = 5.0f,

    // Soporte para imágenes locales (R.drawable.xxx)
    val imagenProductoRes: Int? = null,
    val imagenVendedorRes: Int? = null,

    // Soporte para fotos dinámicas tomadas desde la Cámara o seleccionadas de la Galería
    val imagenProductoUri: String? = null,
    val imagenVendedorUri: String? = null
)