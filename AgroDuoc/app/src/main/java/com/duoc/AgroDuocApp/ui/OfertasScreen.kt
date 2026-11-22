package com.duoc.AgroDuocApp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalOffer
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
import com.duoc.AgroDuocApp.model.OfertaDto
import com.duoc.AgroDuocApp.repository.UserRepository
import com.duoc.AgroDuocApp.ui.theme.CremaFondo
import com.duoc.AgroDuocApp.ui.theme.VerdeClaro
import com.duoc.AgroDuocApp.ui.theme.VerdeOscuro
import com.duoc.AgroDuocApp.ui.theme.VerdePrincipal
import com.duoc.AgroDuocApp.ui.theme.VerdeSuave
import com.duoc.AgroDuocApp.viewmodel.OfertaViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OfertasScreen(
    onLogout: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToPerfil: () -> Unit,
    onNavigateToInfo: () -> Unit,
    onNavigateToCrearOferta: () -> Unit,
    ofertaViewModel: OfertaViewModel = viewModel()
) {
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    val ofertas by ofertaViewModel.ofertas.collectAsState() // <--- Lista de ofertas desde Xano
    val cargando by ofertaViewModel.cargando.collectAsState() // <--- Estado de carga
    val error by ofertaViewModel.error.collectAsState() // <--- Mensaje de error si ocurre

    // Carga las ofertas cada vez que se abre la pantalla
    LaunchedEffect(Unit) {
        ofertaViewModel.cargarOfertas()
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
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "ofertas",
                onNavigateToHome = onNavigateToHome,
                onNavigateToPerfil = onNavigateToPerfil,
                onNavigateToInfo = onNavigateToInfo,
                onLogoutClick = { showLogoutDialog = true }
            )
        },
        floatingActionButton = {
            Button(
                onClick = onNavigateToCrearOferta,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear Oferta"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Nueva Oferta")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CremaFondo) // Fondo suave tipo campo
        ) {
            // Header Superior Alegre y Agrícola
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(VerdeOscuro, VerdePrincipal, VerdeClaro)
                        )
                    )
                    .padding(24.dp)
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
                        color = CremaFondo
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Título de la sección
            Text(
                text = "💼 Ofertas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Lista Deslizable de Ofertas desde Xano
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (cargando) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = VerdePrincipal)
                        }
                    }
                } else if (error != null) {
                    item {
                        Text(
                            text = "Error: $error",
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else if (ofertas.isEmpty()) {
                    item {
                        Text(
                            text = "No hay ofertas registradas en Xano aún.",
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(ofertas, key = { it.id }) { ofertaDto ->
                        OfertaDtoItem(oferta = ofertaDto)
                    }
                }
            }
        }
    }
}

@Composable
fun OfertaDtoItem(
    oferta: OfertaDto
) {
    val formatoClp = remember { NumberFormat.getNumberInstance(Locale("es", "CL")) } // <--- Da formato CLP (ej: 18.000)
    val precioHaFormateado = formatoClp.format(oferta.precio_por_ha)
    val precioTotalFormateado = formatoClp.format(oferta.precio_total_iva)

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
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = VerdePrincipal
                    )
                    Text(
                        text = "Solicitud #${oferta.solicitud_id}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro
                    )
                }

                Surface(
                    color = VerdeSuave,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = oferta.estado,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VerdePrincipal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Aplicador #${oferta.aplicador_id}",
                fontSize = 13.sp,
                color = Color.DarkGray
            )
            Text(
                text = "Precio por ha: $precioHaFormateado CLP",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = VerdePrincipal
            )
            Text(
                text = "Total con IVA: $precioTotalFormateado CLP",
                fontSize = 13.sp,
                color = Color.DarkGray
            )
        }
    }
}