package com.duoc.AgroDuocApp.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.AgroDuocApp.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Crear tu cuenta",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterChip(
                selected = viewModel.selectedRole == "Productor",
                onClick = { viewModel.selectedRole = "Productor" },
                label = { Text("Soy un productor") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = viewModel.selectedRole == "Aplicador",
                onClick = { viewModel.selectedRole = "Aplicador" },
                label = { Text("Soy un aplicador") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.nombre,
            onValueChange = { viewModel.nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.apellido,
            onValueChange = { viewModel.apellido = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.rut,
            onValueChange = { viewModel.rut = it },
            label = { Text("RUT (ej: 12345678-9)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.telefono,
            onValueChange = { viewModel.telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo obligatorio para guardar las credenciales de sesión
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        viewModel.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (viewModel.validarRegistro()) {
                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    onRegisterSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
        ) {
            Text("Registrarse", fontSize = 16.sp)
        }

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = Color.Gray)
        }
    }
}