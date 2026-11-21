package com.duoc.AgroDuocApp.ui

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.AgroDuocApp.model.User
import com.duoc.AgroDuocApp.repository.UserRepository
import com.duoc.AgroDuocApp.ui.theme.CremaFondo
import com.duoc.AgroDuocApp.ui.theme.VerdeClaro
import com.duoc.AgroDuocApp.ui.theme.VerdeOscuro
import com.duoc.AgroDuocApp.ui.theme.VerdePrincipal

@Composable
fun PerfilScreen(
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToInfo: () -> Unit = {},
    onCambiarFotoClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }

    var usuario by remember { mutableStateOf<User?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val rutActivo = userRepository.obtenerSesionActiva()
        if (rutActivo != null) {
            usuario = userRepository.getUserByRut(rutActivo)
        }
        cargando = false
    }

    // Diálogo de confirmación para el botón "Salir" de la barra inferior
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Deseas cerrar tu sesión actual en TecMundo Connect?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        userRepository.cerrarSesion()
                        Toast.makeText(context, "Sesión cerrada con éxito", Toast.LENGTH_SHORT).show()
                        onLogout()
                    }
                ) {
                    Text("Cerrar Sesión", color = VerdePrincipal)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "perfil",
                onNavigateToHome = onNavigateToHome,
                onNavigateToPerfil = { },
                onNavigateToInfo = onNavigateToInfo,
                onLogoutClick = { showLogoutDialog = true }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CremaFondo)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1. CABECERA VERDE CON FOTO DE PERFIL

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(VerdeOscuro, VerdePrincipal, VerdeClaro)
                        )
                    )
                    .padding(vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.5.dp, VerdeOscuro, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val tieneFoto = !usuario?.fotoPerfilUri.isNullOrBlank()
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.size(80.dp),
                            tint = if (tieneFoto) VerdePrincipal else VerdeOscuro
                        )
                    }

                    // Botón "+"
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(CremaFondo)
                            .border(2.dp, VerdeOscuro, CircleShape)
                            .clickable { onCambiarFotoClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Cambiar foto",
                            tint = VerdeOscuro,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }


            // 2. SECCIÓN DE DATOS DEL USUARIO

            if (cargando) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VerdePrincipal)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Datos",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

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

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
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
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.5.dp, VerdePrincipal),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp),
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

            if (esPassword && valor.isNotBlank()) {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Ver contraseña",
                        tint = VerdeOscuro
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