package com.duoc.AgroDuocApp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.AgroDuocApp.model.OfertaDto
import com.duoc.AgroDuocApp.model.SolicitudDto
import com.duoc.AgroDuocApp.ui.theme.CremaFondo
import com.duoc.AgroDuocApp.ui.theme.VerdeClaro
import com.duoc.AgroDuocApp.ui.theme.VerdeMentaTexto
import com.duoc.AgroDuocApp.ui.theme.VerdeOscuro
import com.duoc.AgroDuocApp.ui.theme.VerdePrincipal
import com.duoc.AgroDuocApp.ui.theme.VerdeSuave
import com.duoc.AgroDuocApp.viewmodel.SolicitudViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetalleSolicitudProductorScreen(
    solicitudId: Int,
    onBackClick: () -> Unit,
    solicitudViewModel: SolicitudViewModel = viewModel()
) {
    val context = LocalContext.current
    val solicitudesXano by solicitudViewModel.solicitudesProductorXano.collectAsState()
    val cargandoXano by solicitudViewModel.cargandoXano.collectAsState()

    // Si la lista desde Xano aún está vacía, dispara la carga del productor
    LaunchedEffect(solicitudId) {
        if (solicitudesXano.isEmpty()) {
            solicitudViewModel.cargarDatosProductor()
        }
    }

    val solicitud = solicitudesXano.find { it.id == solicitudId }

    var estadoSolicitud by remember(solicitud?.estado) {
        mutableStateOf(solicitud?.estado ?: "Pendiente")
    }
    var ofertaAceptadaId by remember { mutableStateOf<Int?>(null) }

    // Creacion de Datos ficticios usando el modelo OfertaDto
    val ofertasSimuladas = remember(solicitudId) {
        listOf(
            OfertaDto(
                id = 1,
                created_at = 1732200000000L,
                solicitud_id = solicitudId,
                aplicador_id = 101,
                precio_por_ha = 18000,
                precio_total_iva = 214200,
                estado = "Pendiente",
                calificacion_otorgada = 5
            ),
            OfertaDto(
                id = 2,
                created_at = 1732203600000L,
                solicitud_id = solicitudId,
                aplicador_id = 102,
                precio_por_ha = 16500,
                precio_total_iva = 196350,
                estado = "Pendiente",
                calificacion_otorgada = 4
            ),
            OfertaDto(
                id = 3,
                created_at = 1732207200000L,
                solicitud_id = solicitudId,
                aplicador_id = 103,
                precio_por_ha = 19500,
                precio_total_iva = 232050,
                estado = "Pendiente",
                calificacion_otorgada = 5
            )
        )
    }

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
                            text = "Detalle Solicitud",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Revisa las ofertas recibidas",
                            fontSize = 13.sp,
                            color = VerdeMentaTexto
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CremaFondo)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                when {
                    solicitud != null -> {
                        TarjetaResumenSolicitud(
                            solicitud = solicitud,
                            estado = estadoSolicitud
                        )
                    }
                    cargandoXano -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = VerdePrincipal,
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.5.dp
                                )
                                Text(
                                    text = "Cargando detalle de la solicitud...",
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                    else -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No se encontró la solicitud #$solicitudId.",
                                modifier = Modifier.padding(16.dp),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }

            // Estructura de la parte de Ofertas
            item {
                Text(
                    text = "Ofertas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Se construyen las Ofertas con las ofertas Ficticias declaradas al inicio
            items(ofertasSimuladas, key = { it.id }) { oferta ->
                val esAceptada = ofertaAceptadaId == oferta.id
                TarjetaOfertaItem(
                    oferta = oferta,
                    esAceptada = esAceptada,
                    hayOtraAceptada = ofertaAceptadaId != null && !esAceptada,
                    onAceptarOferta = {     // <--- Al apretar el boton "Aceptar Oferta" la oferta cambia de estado a aceptada
                        ofertaAceptadaId = oferta.id
                        estadoSolicitud = "Aceptada"
                        Toast.makeText(     // <--- Aparece el mensaje debajo de que la oferta fue aceptada
                            context,
                            "Oferta del Aplicador #${oferta.aplicador_id} aceptada",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onComunicarse = {       // <--- Si se aprieta el boton "Comunicarse" aparece el mensaje
                        Toast.makeText(
                            context,
                            "Contactando a Aplicador #${oferta.aplicador_id}...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}

@Composable
private fun TarjetaResumenSolicitud(
    solicitud: SolicitudDto,
    estado: String
) {
    // Es la tarjeta de la solicitud
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VerdeSuave),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Agriculture,
                    contentDescription = "Solicitud",
                    tint = VerdePrincipal,
                    modifier = Modifier.size(34.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Producto: ${solicitud.tipo_producto}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro
                )
                Text(
                    text = "Superficie: ${solicitud.superficie_ha} ha",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Aplicación: ${solicitud.tipo_aplicacion}",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                if (!solicitud.comentarios.isNullOrBlank()) {
                    Text(
                        text = "Detalles: ${solicitud.comentarios}",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Surface(
                color = if (estado.equals("Aceptada", ignoreCase = true)) VerdeSuave else Color(0xFFFFF8E1),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = estado,
                    color = if (estado.equals("Aceptada", ignoreCase = true)) VerdePrincipal else Color(0xFFF57F17),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun TarjetaOfertaItem(
    oferta: OfertaDto,
    esAceptada: Boolean,    // Estado si es que la oferta esta aceptada o no
    hayOtraAceptada: Boolean,
    onAceptarOferta: () -> Unit,
    onComunicarse: () -> Unit
) {
    val formatoClp = remember { NumberFormat.getNumberInstance(Locale("es", "CL")) }    // <--- Le da el formato al dinero (16000 -> 16.000)
    val precioHaFormateado = formatoClp.format(oferta.precio_por_ha) // Usan la variable formatoClp
    val precioTotalFormateado = formatoClp.format(oferta.precio_total_iva)
    val estrellas = oferta.calificacion_otorgada ?: 0

    // Se construye la plantilla de las ofertas
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
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(VerdeSuave),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Aplicador",
                        tint = VerdePrincipal
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Aplicador #${oferta.aplicador_id}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro
                    )
                    Text(
                        text = "Precio por ha: $precioHaFormateado CLP",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VerdePrincipal
                    )
                    Text(
                        text = "Total con IVA: $precioTotalFormateado CLP",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "Calificación: ",
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                        repeat(5) { index ->
                            Icon(       //
                                imageVector = Icons.Default.Star,
                                contentDescription = "Estrella",
                                tint = if (index < estrellas) Color(0xFFFFB300) else Color.LightGray, // Modela las estrellas segun la calificacion del prestador
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onAceptarOferta,
                    enabled = !hayOtraAceptada && !esAceptada,  // Solo se activa si no hay ninguna oferta aceptada
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (esAceptada) "Oferta Aceptada" else "Aceptar Oferta",
                        fontSize = 13.sp
                    )
                }

                OutlinedButton(
                    onClick = onComunicarse,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeOscuro),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Comunicarse",
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}