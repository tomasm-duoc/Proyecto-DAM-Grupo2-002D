package com.duoc.AgroDuocApp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

// Función de utilidad global para decodificar cadenas Base64 en ImageBitmap de Compose
fun base64ToImageBitmap(base64Str: String?): ImageBitmap? {
    if (base64Str.isNullOrBlank()) return null
    return try {
        val cleanStr = if (base64Str.contains(",")) base64Str.substringAfter(",") else base64Str
        val decodedBytes = Base64.decode(cleanStr, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Función de utilidad global para convertir Bitmap a cadenas Base64
fun bitmapToBase64(bitmap: Bitmap, quality: Int = 70): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}
