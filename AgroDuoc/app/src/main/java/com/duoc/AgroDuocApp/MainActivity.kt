package com.duoc.AgroDuocApp

import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vinculación básica de elementos del XML

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Evento al presionar "Iniciar Sesión"

        btnLogin.setOnClickListener {
            Toast.makeText(this, "Próximamente: Pantalla de Login", Toast.LENGTH_SHORT).show()
        }

        // Evento al presionar "Crear una Cuenta"

        btnRegister.setOnClickListener {
            Toast.makeText(this, "Próximamente: Pantalla de Registro", Toast.LENGTH_SHORT).show()
        }
    }
}