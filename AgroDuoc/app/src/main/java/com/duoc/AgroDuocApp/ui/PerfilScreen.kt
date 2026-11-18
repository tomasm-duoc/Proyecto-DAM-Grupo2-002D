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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.AgroDuocApp.model.User
import com.duoc.AgroDuocApp.repository.UserRepository

@Composable
fun PerfilScreen(
    onCambiarFotoClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }

    // Estados para guardar el usuario real cargado desde Room
    var usuario by remember { mutableStateOf<User?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var isNavigatingBack by remember { mutableStateOf(false) }

    // Al abrir la pantalla, busca el RUT activo en SharedPreferences y consulta Room
    LaunchedEffect(Unit) {
        val rutActivo = userRepository.obtenerSesionActiva()
        if (rutActivo != null) {
            usuario = userRepository.getUserByRut(rutActivo)
        }
        cargando = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 0. Boton para volver

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

        // Si aún está consultando la base de datos Room, muestra un indicador de carga
        if (cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF2E7D32))
            }
        } else {

            // 1. SECCIÓN AVATAR + BOTÓN "+"

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
                    // Si el usuario tomó foto en RegisterScreen, el ícono se pinta verde
                    val tieneFoto = !usuario?.fotoPerfilUri.isNullOrBlank()
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.size(80.dp),
                        tint = if (tieneFoto) Color(0xFF2E7D32) else Color.Black
                    )
                }

                // Botón "+"
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


            // 2. Datos del usuario (Definidos a la hora de registrar su cuenta)

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

                // Muestra los datos traídos desde Room (si es null, muestra vacío)
                CampoDatoPerfil(etiqueta = "RUT", valor = usuario?.rut ?: "")
                CampoDatoPerfil(etiqueta = "Nombre", valor = usuario?.nombre ?: "")
                CampoDatoPerfil(etiqueta = "Apellido", valor = usuario?.apellido ?: "")
                CampoDatoPerfil(etiqueta = "Email", valor = usuario?.email ?: "")
                CampoDatoPerfil(etiqueta = "Teléfono", valor = usuario?.telefono ?: "")
                CampoDatoPerfil(etiqueta = "Rol", valor = usuario?.rol ?: "")
                CampoDatoPerfil(
                    etiqueta = "Contraseña",
                    valor = usuario?.password ?: "",
                    esPassword = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            // 3. Calificacion (Podria borrarse despues, datos predeterminados)

            val clasificacion = 4.6f

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Clasificación: $clasificacion",
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
                    val estrellasLlenas = clasificacion.toInt()
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

            //Permite mostrar la contraseña y ocultarla
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