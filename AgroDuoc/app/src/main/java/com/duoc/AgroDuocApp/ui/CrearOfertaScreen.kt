package com.duoc.AgroDuocApp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.AgroDuocApp.model.SolicitudDto
import com.duoc.AgroDuocApp.ui.theme.CremaFondo
import com.duoc.AgroDuocApp.ui.theme.VerdeClaro
import com.duoc.AgroDuocApp.ui.theme.VerdeMentaTexto
import com.duoc.AgroDuocApp.ui.theme.VerdeOscuro
import com.duoc.AgroDuocApp.ui.theme.VerdePrincipal
import com.duoc.AgroDuocApp.ui.theme.VerdeSuave
import com.duoc.AgroDuocApp.viewmodel.OfertaViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearOfertaScreen(
    onBackClick: () -> Unit,
    onOfertaCreada: () -> Unit,
    ofertaViewModel: OfertaViewModel = viewModel()
) {
    val context = LocalContext.current
    val formatoClp = remember { NumberFormat.getNumberInstance(Locale("es", "CL")) } // <--- Formato moneda CLP (ej: 18.000)

    val solicitudesDisponibles by ofertaViewModel.solicitudesDisponibles.collectAsState() // <--- Solicitudes traídas desde Xano
    val cargando by ofertaViewModel.cargando.collectAsState()
    val error by ofertaViewModel.error.collectAsState()

    // Carga las solicitudes disponibles en Xano al entrar a la pantalla
    LaunchedEffect(Unit) {
        ofertaViewModel.cargarSolicitudesParaOfertar()
    }

    var menuExpandido by remember { mutableStateOf(false) } // <--- Controla el menú desplegable de solicitudes
    var solicitudSeleccionada by remember { mutableStateOf<SolicitudDto?>(null) } // <--- Solicitud elegida para ofertar
    var precioPorHaTexto by remember { mutableStateOf("") } // <--- Input del precio por hectárea

    // Selecciona automáticamente la primera solicitud cuando llegan desde Xano
    LaunchedEffect(solicitudesDisponibles) {
        if (solicitudSeleccionada == null && solicitudesDisponibles.isNotEmpty()) {
            solicitudSeleccionada = solicitudesDisponibles.first()
        }
    }

    // Cálculo automático del total neto y total con IVA (19% en Chile)
    val precioPorHa = precioPorHaTexto.toIntOrNull() ?: 0
    val superficieHa = solicitudSeleccionada?.superficie_ha ?: 0.0
    val subtotalNeto = (precioPorHa * superficieHa).roundToInt()
    val totalConIva = (subtotalNeto * 1.19).roundToInt()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(VerdeOscuro, VerdePrincipal, VerdeClaro)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Nueva Oferta",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Selecciona una solicitud y cotiza tu servicio",
                            fontSize = 13.sp,
                            color = VerdeMentaTexto
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CremaFondo)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Selector desplegable de solicitudes de Xano
            Text(
                text = "Seleccionar Solicitud",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { menuExpandido = true }, // <--- Abre el menú de solicitudes
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = VerdeOscuro
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = solicitudSeleccionada?.let {
                                "Solicitud #${it.id} · ${it.tipo_producto} (${it.superficie_ha} ha)"
                            } ?: "Seleccionar solicitud de Xano...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Desplegar solicitudes",
                            tint = VerdePrincipal
                        )
                    }
                }

                DropdownMenu(
                    expanded = menuExpandido,
                    onDismissRequest = { menuExpandido = false }
                ) {
                    if (solicitudesDisponibles.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No hay solicitudes disponibles en Xano") },
                            onClick = { menuExpandido = false }
                        )
                    } else {
                        solicitudesDisponibles.forEach { solicitud ->
                            DropdownMenuItem(
                                text = {
                                    Text("#${solicitud.id} - ${solicitud.tipo_producto} (${solicitud.superficie_ha} ha)")
                                },
                                onClick = {
                                    solicitudSeleccionada = solicitud // <--- Guarda la solicitud elegida
                                    menuExpandido = false
                                }
                            )
                        }
                    }
                }
            }

            // Tarjeta resumen de la solicitud elegida (similar al wireframe)
            solicitudSeleccionada?.let { sol ->
                Text(
                    text = "Detalle de solicitud",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro
                )

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
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(VerdeSuave),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Agriculture,
                                    contentDescription = "Solicitud",
                                    tint = VerdePrincipal,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Producto: ${sol.tipo_producto}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VerdeOscuro
                                )
                                Text(
                                    text = "Superficie: ${sol.superficie_ha} ha",
                                    fontSize = 13.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "Aplicación: ${sol.tipo_aplicacion}",
                                    fontSize = 13.sp,
                                    color = Color.DarkGray
                                )
                            }

                            Surface(
                                color = Color(0xFFFFF8E1),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = sol.estado,
                                    color = Color(0xFFF57F17),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        if (!sol.comentarios.isNullOrBlank()) {
                            HorizontalDivider(color = CremaFondo)
                            Text(
                                text = "Más información y comentarios",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = VerdeOscuro
                            )
                            Text(
                                text = sol.comentarios,
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }

            // Campo para ingresar precio por hectárea y cálculo automático + IVA
            Text(
                text = "Cotización",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )

            OutlinedTextField(
                value = precioPorHaTexto,
                onValueChange = { nuevoValor ->
                    // Permite solo dígitos numéricos
                    if (nuevoValor.all { it.isDigit() }) {
                        precioPorHaTexto = nuevoValor
                    }
                },
                label = { Text("Ingresar Precio por Ha (CLP)") },
                placeholder = { Text("Ej: 18000") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = VerdePrincipal
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Resumen dinámico de precio neto y precio total con IVA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VerdeSuave)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Subtotal neto (${superficieHa} ha): $${formatoClp.format(subtotalNeto)} CLP",
                        fontSize = 13.sp,
                        color = VerdeOscuro
                    )
                    Text(
                        text = "El precio total de la aplicación será de $${formatoClp.format(totalConIva)} CLP (IVA incluido)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdePrincipal
                    )
                }
            }

            if (error != null) {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Botón principal para enviar y guardar la oferta en Xano
            Button(
                onClick = {
                    val sol = solicitudSeleccionada
                    if (sol == null) {
                        Toast.makeText(context, "Selecciona una solicitud primero", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (precioPorHa <= 0) {
                        Toast.makeText(context, "Ingresa un precio por hectárea válido", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    ofertaViewModel.crearOferta(
                        solicitudId = sol.id,
                        precioPorHa = precioPorHa,
                        precioTotalIva = totalConIva,
                        onExito = {
                            Toast.makeText(context, "¡Oferta enviada con éxito!", Toast.LENGTH_SHORT).show()
                            onOfertaCreada() // <--- Regresa a OfertasScreen donde aparecerá la nueva oferta
                        }
                    )
                },
                enabled = !cargando && solicitudSeleccionada != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Ofertar !!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}