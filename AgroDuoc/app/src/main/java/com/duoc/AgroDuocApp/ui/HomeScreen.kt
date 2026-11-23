package com.duoc.AgroDuocApp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.AgroDuocApp.repository.UserRepository
import com.duoc.AgroDuocApp.ui.theme.CremaFondo
import com.duoc.AgroDuocApp.ui.theme.VerdeOscuro
import com.duoc.AgroDuocApp.ui.theme.VerdePrincipal
import com.duoc.AgroDuocApp.ui.theme.VerdeSuave
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat

@Composable
fun HomeScreen(
    nombreUsuario: String? = null,
    onLogout: () -> Unit = {},
    onNavigateToPerfil: () -> Unit = {},
    onNavigateToInfo: () -> Unit = {},
    onIrASolicitudes: () -> Unit = {},
    onIrAOfertas: () -> Unit = {}
) {
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permiso = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permiso != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Deseas cerrar tu sesión actual en TecMundo Connect?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        val userRepository = UserRepository(context)
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
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(VerdePrincipal, VerdeOscuro)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "🌱 TecMundo Connect",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (!nombreUsuario.isNullOrBlank()) {
                            "Hola, $nombreUsuario · Mercado y Servicios Agrícolas"
                        } else {
                            "Mercado y Servicios Agrícolas"
                        },
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "home",
                onNavigateToHome = { }, // Ya estás en Home
                onNavigateToPerfil = onNavigateToPerfil,
                onNavigateToInfo = onNavigateToInfo,
                onLogoutClick = { showLogoutDialog = true }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CremaFondo)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "¿Qué deseas revisar hoy?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )

            AccesoModuloCard(
                titulo = "Solicitudes",
                subtitulo = "Revisa o publica solicitudes de aplicación agrícola",
                icono = Icons.Default.Assignment,
                onClick = onIrASolicitudes
            )

            AccesoModuloCard(
                titulo = "Ofertas",
                subtitulo = "Consulta o envía ofertas para servicios disponibles",
                icono = Icons.Default.LocalOffer,
                onClick = onIrAOfertas
            )
        }
    }
}

@Composable
private fun AccesoModuloCard(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(VerdeSuave, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = titulo,
                    tint = VerdePrincipal
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = titulo,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B2A1E)
                )
                Text(
                    text = subtitulo,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = VerdePrincipal
            )
        }
    }
}