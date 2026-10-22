package com.duoc.AgroDuocApp.ui

import com.duoc.AgroDuocApp.model.Producto


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun HomeScreen(
    // Datos editables de prueba
    productos: List<Producto> = List(5) { Producto() }
) {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Título Superior
            Text(
                text = "TecMundo\nConnect",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C2C2C),
                textAlign = TextAlign.Center,
                lineHeight = 36.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

            // Lista Deslizable de Productos
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(productos) { producto ->
                    ProductoItem(producto = producto)
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del Producto (Placeholder)
        Box(
            modifier = Modifier
                .size(width = 90.dp, height = 55.dp)
                .border(1.dp, Color.Gray)
                .background(Color.White)
        )

        // Nombre y Precio
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = producto.nombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = producto.precio,
                fontSize = 13.sp,
                color = Color.DarkGray
            )
        }

        // Vendedor e Imagen + Estrellas
        Column(
            horizontalAlignment = Alignment.End
        ) {
            // Imagen del Vendedor (Placeholder)
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .border(1.dp, Color.Gray)
                    .background(Color.White)
            )

            Text(
                text = producto.nombreVendedor,
                fontSize = 11.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Estrellas de Calificación
            Row {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Estrella",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.DarkGray
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Info, contentDescription = "Info") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") }
        )
    }
}