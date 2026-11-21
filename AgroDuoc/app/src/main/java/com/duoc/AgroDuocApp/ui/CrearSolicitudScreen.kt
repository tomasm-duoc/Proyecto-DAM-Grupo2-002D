package com.duoc.AgroDuocApp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.AgroDuocApp.ui.theme.CremaFondo
import com.duoc.AgroDuocApp.ui.theme.GrisClaro
import com.duoc.AgroDuocApp.ui.theme.VerdeOscuro
import com.duoc.AgroDuocApp.ui.theme.VerdePrincipal
import com.duoc.AgroDuocApp.ui.theme.VerdeSuave
import com.duoc.AgroDuocApp.viewmodel.SolicitudViewModel

@Composable
fun CrearSolicitudScreen(
    solicitudViewModel: SolicitudViewModel = viewModel(),
    onSolicitudGuardada: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Cuando la funcion de guardarSolicitud() (de SolicitudViewModel) termine, este se activa
    LaunchedEffect(solicitudViewModel.guardadoExitoso) {
        if(solicitudViewModel.guardadoExitoso){
            solicitudViewModel.quitarEstadoGuardado()
            onSolicitudGuardada()
        }
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
                Column {
                    Text(
                        text = "Nueva Solicitud",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Registra los datos del servicio agrícola",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CremaFondo)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Ubicación referencial (Ficticia)
                    OutlinedTextField(
                        value = solicitudViewModel.ubicacion,
                        onValueChange = { solicitudViewModel.ubicacion = it },
                        label = { Text("Ubicación / Zona referencial") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Tipo de cultivo
                    OutlinedTextField(
                        value = solicitudViewModel.tipoCultivo,
                        onValueChange = { solicitudViewModel.tipoCultivo = it },
                        label = { Text("Tipo de cultivo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Tipo de producto (Se podria cambiar por una caja desplegable)
                    OutlinedTextField(
                        value = solicitudViewModel.tipoProducto,
                        onValueChange = { solicitudViewModel.tipoProducto = it },
                        label = { Text("Tipo de producto") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Superficie
                    OutlinedTextField(
                        value = solicitudViewModel.superficie,
                        onValueChange = { solicitudViewModel.superficie = it },
                        label = { Text("Superficie (ej: 5 hás)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Fecha o ventana de disponibilidad
                    OutlinedTextField(
                        value = solicitudViewModel.fechaDisponibilidad,
                        onValueChange = { solicitudViewModel.fechaDisponibilidad = it },
                        label = { Text("Fecha o ventana de disponibilidad") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // ¿Tiene Agua?
                    Column {
                        Text(
                            text = "¿Tiene Agua?",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = solicitudViewModel.tieneAgua,
                                onClick = { solicitudViewModel.tieneAgua = true }
                            )
                            Text("Sí")
                            Spacer(modifier = Modifier.width(24.dp))
                            RadioButton(
                                selected = !solicitudViewModel.tieneAgua,
                                onClick = { solicitudViewModel.tieneAgua = false }
                            )
                            Text("No")
                        }
                    }

                    // Aplicación (Diurna / Nocturna)
                    Column {
                        Text(
                            text = "Aplicación",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = solicitudViewModel.aplicacion == "Diurna",
                                onClick = { solicitudViewModel.aplicacion = "Diurna" }
                            )
                            Text("Diurna")
                            Spacer(modifier = Modifier.width(24.dp))
                            RadioButton(
                                selected = solicitudViewModel.aplicacion == "Nocturna",
                                onClick = { solicitudViewModel.aplicacion = "Nocturna" }
                            )
                            Text("Nocturna")
                        }
                    }

                    // Obstaculos
                    Column {
                        Text(
                            text = "Obstáculos",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        val obstaculos = listOf(
                            "Antenas",
                            "Cables",
                            "Torres de alta tensión",
                            "Otros"
                        )
                        obstaculos.forEach { obs ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = solicitudViewModel.obstaculosSeleccionados.contains(obs),
                                    onCheckedChange = { solicitudViewModel.toggleObstaculo(obs) }
                                )
                                Text(text = obs, fontSize = 14.sp)
                            }
                        }
                    }

                    // Comentarios adicionales
                    OutlinedTextField(
                        value = solicitudViewModel.comentarios,
                        onValueChange = { solicitudViewModel.comentarios = it },
                        label = { Text("Comentarios adicionales") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Espacios visuales de fotos
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Fotografías",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                modifier = Modifier.size(68.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = VerdeSuave
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Agregar foto",
                                        tint = VerdePrincipal
                                    )
                                }
                            }
                            repeat(3) {
                                Surface(
                                    modifier = Modifier.size(68.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = GrisClaro
                                ) {}
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Botón Publicar Solicitud
                    Button(
                        onClick = { solicitudViewModel.guardarSolicitud() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Publicar Solicitud",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}