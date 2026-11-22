package com.duoc.AgroDuocApp.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.AgroDuocApp.repository.UserRepository
import com.duoc.AgroDuocApp.ui.theme.CremaFondo
import com.duoc.AgroDuocApp.ui.theme.VerdeClaro
import com.duoc.AgroDuocApp.ui.theme.VerdeOscuro
import com.duoc.AgroDuocApp.ui.theme.VerdePrincipal
import com.duoc.AgroDuocApp.utils.base64ToImageBitmap
import com.duoc.AgroDuocApp.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToInfo: () -> Unit = {},
    viewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Diálogo de confirmación para cerrar sesión
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

    // Diálogo de confirmación para eliminar cuenta
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Cuenta") },
            text = { Text("¿Estás seguro de que deseas eliminar tu cuenta permanentemente? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.eliminarCuenta(
                            onSuccess = {
                                Toast.makeText(context, "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show()
                                onLogout()
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
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
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, VerdeOscuro, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val bitmapImage = remember(viewModel.usuarioDto?.foto_perfil) {
                        base64ToImageBitmap(viewModel.usuarioDto?.foto_perfil)
                    }

                    if (bitmapImage != null) {
                        Image(
                            bitmap = bitmapImage,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.size(80.dp),
                            tint = VerdeOscuro
                        )
                    }
                }
            }

            // 2. SECCIÓN DE DATOS DEL USUARIO
            if (viewModel.cargando) {
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
                        text = "Datos de Perfil",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    CampoDatoPerfil(etiqueta = "RUT", valor = viewModel.usuarioDto?.rut ?: "")
                    CampoDatoPerfil(etiqueta = "Nombre", valor = viewModel.usuarioDto?.nombre ?: "")
                    CampoDatoPerfil(etiqueta = "Apellido", valor = viewModel.usuarioDto?.apellido ?: "")
                    CampoDatoPerfil(etiqueta = "Email", valor = viewModel.usuarioDto?.email ?: "")
                    CampoDatoPerfil(etiqueta = "Teléfono", valor = viewModel.usuarioDto?.telefono ?: "")
                    CampoDatoPerfil(etiqueta = "Rol", valor = viewModel.usuarioDto?.rol ?: "")

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón Eliminar Cuenta
                    Button(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar cuenta", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Eliminar cuenta permanentemente", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun CampoDatoPerfil(
    etiqueta: String,
    valor: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.5.dp, Color.LightGray),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$etiqueta: $valor",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PerfilScreenPreview() {
    PerfilScreen(
        onLogout = {},
        onNavigateToHome = {},
        onNavigateToInfo = {}
    )
}
