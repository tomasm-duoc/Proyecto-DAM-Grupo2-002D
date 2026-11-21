package com.duoc.AgroDuocApp.ui

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.AgroDuocApp.ui.theme.CremaFondo
import com.duoc.AgroDuocApp.ui.theme.VerdeClaro
import com.duoc.AgroDuocApp.ui.theme.VerdeMentaTexto
import com.duoc.AgroDuocApp.ui.theme.VerdeOscuro
import com.duoc.AgroDuocApp.ui.theme.VerdePrincipal
import com.duoc.AgroDuocApp.ui.theme.VerdeSuave

@Composable
fun FaqsScreen(
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    val preguntasFrecuentes = listOf(   // <--- Se crea una lista para guardar las preguntas con sus respuestas
        "¿Que es TecMundo Connect?" to
            "Es una plataforma móvil diseñada para conectar a productores agrícolas y forestales con prestadores especializados en fumigación y aplicación mediante drones.",
        "¿Cómo publico una solicitud de servicio?" to
                "Desde la pantalla de inicio, presiona el botón '+ Nueva Solicitud', completa los datos del cultivo, producto, superficie y condiciones del terreno, y luego presiona 'Publicar Solicitud'.",
        "¿Por qué debo indicar obstáculos como antenas o cables?" to
                "Informar la presencia de antenas, cables o torres de alta tensión permite planificar vuelos seguros y evitar riesgos operativos durante la aplicación con drones.",
        "¿Qué diferencia hay entre aplicación diurna y nocturna?" to
                "La elección depende del tipo de producto, las condiciones climáticas y la disponibilidad del predio para optimizar la cobertura y seguridad de la operación."
    )

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
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    IconButton(onClick = onBackClick){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column{
                        Text(
                            text = "Informacion y FAQs",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Conoce más sobre TecMundo Connect",
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
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ¿Quines Somos?
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "¿Quienes somos?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro
                    )
                    Text(
                        text = "En TecMundo impulsamos la agricultura y silvicultura de precisión mediante soluciones tecnológicas accesibles. A través de TecMundo Connect facilitamos la coordinación directa entre agricultores y operadores de drones certificados para aplicaciones eficientes y seguras.",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )
                }
            }
            // Preguntas Frecuentes
            Text(
                text = "Preguntas Frecuentes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )

            preguntasFrecuentes.forEach { (pregunta, respuesta) ->  // <--- Pasa pregunta y respuesta (en la lista de preguntasFrecuentes), las modela con la plantilla FaqExpandableCard
                FaqExpandableCard(
                    pregunta = pregunta,
                    respuesta = respuesta,
                    colorTitulo = VerdeOscuro,
                    colorIcono = VerdePrincipal
                )
            }
        }
    }
}

@Composable
private fun FaqExpandableCard(  // <--- Se crea una plantilla para mostrar las preguntas frecuentes
    pregunta: String,
    respuesta: String,
    colorTitulo: Color,
    colorIcono: Color
){
    var expandida by remember { mutableStateOf(false) } // <--- Se declara si la card esta expandida o no con un boolean

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable{ expandida = !expandida }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Se coloca la pregunta
                Text(
                    text = pregunta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorTitulo,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if(expandida) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if(expandida) "Contraer" else "Expandir",
                    tint = colorIcono
                )
            }
            // Si la card esta expandida aparece la respuesta
            if(expandida){
                HorizontalDivider(color = VerdeSuave)
                Text(
                    text = respuesta,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
            }
        }
    }
}