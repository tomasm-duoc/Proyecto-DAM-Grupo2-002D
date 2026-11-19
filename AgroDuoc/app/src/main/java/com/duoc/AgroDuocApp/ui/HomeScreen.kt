package com.duoc.AgroDuocApp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.AgroDuocApp.model.Solicitud
import com.duoc.AgroDuocApp.repository.UserRepository
import com.duoc.AgroDuocApp.viewmodel.SolicitudViewModel

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToCrearSolicitud: () -> Unit,
    solicitudViewModel: SolicitudViewModel = viewModel()
) {
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Observa las solicitudes guardadas en Room desde el ViewModel
    val solicitudes by solicitudViewModel.solicitudes.collectAsState()

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
                    Text("Cerrar Sesión", color = Color(0xFF2E7D32))
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
                onNavigateToPerfil = onNavigateToPerfil,
                onLogoutClick = { showLogoutDialog = true }
            )
        },
        floatingActionButton = {
            CrearSolicitud(
                onNavigateToCrearSolicitud = onNavigateToCrearSolicitud
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF9FBE7)) // Fondo suave tipo campo
        ) {
            // Header Superior Alegre y Agrícola
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF1B4D3E), Color(0xFF2E7D32), Color(0xFF43A047))
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "🌱 TecMundo Connect",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Mercado y Servicios Agrícolas",
                                fontSize = 14.sp,
                                color = Color(0xFFC8E6C9)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Título de la sección
            Text(
                text = "📋 Solicitudes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B4D3E),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Lista Deslizable de Solicitudes desde Room
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(solicitudes, key = { it.id }) { solicitud ->
                    SolicitudItem(solicitud = solicitud)
                }
            }
        }
    }
}

@Composable
fun CrearSolicitud(
    onNavigateToCrearSolicitud: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onNavigateToCrearSolicitud() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2E7D32),
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Crear Solicitud"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Nueva Solicitud")
    }
}

@Composable
fun SolicitudItem(solicitud: Solicitud) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${solicitud.tipoProducto} · ${solicitud.tipoCultivo}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B4D3E)
            )
            Text(
                text = "Superficie: ${solicitud.superficie}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2E7D32)
            )
            Text(
                text = "Ubicación: ${solicitud.ubicacion} · ${solicitud.aplicacion}",
                fontSize = 13.sp,
                color = Color.DarkGray
            )
            if (solicitud.fechaDisponibilidad.isNotBlank()) {
                Text(
                    text = "Disponibilidad: ${solicitud.fechaDisponibilidad}",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onNavigateToPerfil: () -> Unit,
    onLogoutClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF2E7D32)
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2E7D32),
                indicatorColor = Color(0xFFE8F5E9)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToPerfil,
            icon = { Icon(Icons.Default.Person, contentDescription = "Info") },
            label = { Text("Info") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF2E7D32))
        )
        NavigationBarItem(
            selected = false,
            onClick = onLogoutClick,
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión") },
            label = { Text("Salir") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF2E7D32))
        )
    }
}