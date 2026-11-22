package com.duoc.AgroDuocApp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.duoc.AgroDuocApp.model.AuthResponse
import com.duoc.AgroDuocApp.remote.RetrofitClient
import com.duoc.AgroDuocApp.remote.XanoApiService
import com.duoc.AgroDuocApp.viewmodel.RegisterViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import android.util.Patterns
import java.util.regex.Pattern
import io.mockk.mockkStatic

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var application: Application
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var apiService: XanoApiService
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        application = mockk(relaxed = true)
        prefs = mockk(relaxed = true)
        editor = mockk(relaxed = true)
        apiService = mockk(relaxed = true)

        every { application.getSharedPreferences("TecMundoPrefs", Context.MODE_PRIVATE) } returns prefs
        every { prefs.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor

        // Interceptamos RetrofitClient para usar nuestro apiService simulado
        mockkObject(RetrofitClient)
        every { RetrofitClient.getInstance(application) } returns apiService

        viewModel = RegisterViewModel(application)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `validarYRegistrar con datos validos llama a signup y ejecuta onSuccess`() = runTest {
        // Arrange (Llenamos el formulario con datos válidos)
        viewModel.nombre = "Carlos"
        viewModel.apellido = "Muñoz"
        viewModel.rut = "18456789-2"
        viewModel.email = "carlos@agro.cl"
        viewModel.telefono = "+56987654321"
        viewModel.password = "clave123"
        viewModel.selectedRole = "Productor"

        val respuestaSimulada = mockk<AuthResponse> {
            every { authToken } returns "token_falso_12345"
        }
        coEvery { apiService.signup(any()) } returns respuestaSimulada

        var registroExitoso = false

        // Act
        viewModel.validarYRegistrar {
            registroExitoso = true
        }
        advanceUntilIdle() // Espera a que termine la corrutina viewModelScope.launch

        // Assert
        assertTrue(registroExitoso)
        assertNull(viewModel.errorMessage)
        coVerify(exactly = 1) { apiService.signup(any()) }
    }

    @Test
    fun `validarYRegistrar con RUT invalido muestra error y no llama a Xano`() = runTest {
        // Arrange (RUT sin guion)
        viewModel.nombre = "Carlos"
        viewModel.apellido = "Muñoz"
        viewModel.rut = "184567892"
        viewModel.email = "carlos@agro.cl"
        viewModel.telefono = "+56987654321"
        viewModel.password = "clave123"

        // Act
        viewModel.validarYRegistrar { }
        advanceUntilIdle()

        // Assert
        assertEquals("Ingresa un RUT valido (ej: 12345678-9)", viewModel.errorMessage)
        coVerify(exactly = 0) { apiService.signup(any()) }
    }
}