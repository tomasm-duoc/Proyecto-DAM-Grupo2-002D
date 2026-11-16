package com.duoc.AgroDuocApp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// 1. Modelo actualizado con todos tus campos exactos

data class UsuarioPerfil(
    val rut: String = "", // Clave primaria única
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val telefono: String = "",
    val rol: String = "", // "Productor" o "Aplicador"
    val password: String = "",
    val fotoPerfilUri: String? = null, // URI/ruta de la foto capturada con la Cámara
    val clasificacion: Float = 4.6f
)

@Composable
fun PerfilScreen(
    usuario: UsuarioPerfil = UsuarioPerfil(
        rut = "12.345.678-9",
        nombre = "Juan",
        apellido = "Pérez",
        email = "juan.perez@correo.cl",
        telefono = "+56 9 1234 5678",
        rol = "Productor",
        password = "miPassword123",
        fotoPerfilUri = null,
        clasificacion = 4.6f
    ),
    onCambiarFotoClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var isNavigatingBack by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ==========================================
        // 0. BOTÓN VOLVER (TODA LA FILA CLICABLE + ANTI DOBLE CLIC)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = !isNavigatingBack) {
                        isNavigatingBack = true
                        onBackClick()
                    }
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Volver",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }
        // ==========================================
        // 1. SECCIÓN AVATAR + BOTÓN "+"
        // ==========================================
        Box(
            modifier = Modifier.size(130.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF0F0F0))
                    .border(2.dp, Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // <--- AQUÍ VA LA FOTO DE LA CÁMARA (usuario.fotoPerfilUri):
                if (!usuario.fotoPerfilUri.isNullOrBlank()) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto cargada",
                        modifier = Modifier.size(80.dp),
                        tint = Color(0xFF2E7D32)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Sin foto de perfil",
                        modifier = Modifier.size(80.dp),
                        tint = Color.Black
                    )
                }
            }

            // Botón "+" para abrir la cámara
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(2.dp, Color.Black, CircleShape)
                    .clickable { onCambiarFotoClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Cambiar foto",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // 2. SECCIÓN DE TODOS LOS DATOS DEL USUARIO
        // ==========================================
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Datos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )


            CampoDatoPerfil(etiqueta = "RUT", valor = usuario.rut)
            CampoDatoPerfil(etiqueta = "Nombre", valor = usuario.nombre)
            CampoDatoPerfil(etiqueta = "Apellido", valor = usuario.apellido)
            CampoDatoPerfil(etiqueta = "Email", valor = usuario.email)
            CampoDatoPerfil(etiqueta = "Teléfono", valor = usuario.telefono)
            CampoDatoPerfil(etiqueta = "Rol", valor = usuario.rol)
            CampoDatoPerfil(
                etiqueta = "Contraseña",
                valor = usuario.password,
                esPassword = true // Oculta la contraseña con puntos y permite mostrarla
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // 3. SECCIÓN CLASIFICACIÓN Y ESTRELLAS
        // ==========================================
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Clasificación: ${usuario.clasificacion}",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val estrellasLlenas = usuario.clasificacion.toInt()
                for (i in 1..5) {
                    val estaLlena = i <= estrellasLlenas
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (estaLlena) Color.Black else Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (estaLlena) Icons.Default.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Estrella $i",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CampoDatoPerfil(
    etiqueta: String,
    valor: String,
    esPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.5.dp, Color.Black),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val valorFormateado = when {
                valor.isBlank() -> ""
                esPassword && !passwordVisible -> "•".repeat(valor.length)
                else -> valor
            }

            val textoAMostrar = if (valorFormateado.isNotBlank()) {
                "$etiqueta: $valorFormateado"
            } else {
                etiqueta
            }

            Text(
                text = textoAMostrar,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            // Si es contraseña, muestra un botón de ojo para ver/ocultar
            if (esPassword && valor.isNotBlank()) {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Ver contraseña",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PerfilScreenPreview() {
    PerfilScreen()
}