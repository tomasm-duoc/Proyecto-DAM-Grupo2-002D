package com.duoc.AgroDuocApp

import android.app.Application
import com.duoc.AgroDuocApp.model.Solicitud
import com.duoc.AgroDuocApp.repository.AppDatabase
import com.duoc.AgroDuocApp.repository.SolicitudDao
import com.duoc.AgroDuocApp.viewmodel.SolicitudViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SolicitudViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var application: Application
    private lateinit var database: AppDatabase
    private lateinit var solicitudDao: SolicitudDao
    private lateinit var viewModel: SolicitudViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        application = mockk(relaxed = true)
        database = mockk(relaxed = true)
        solicitudDao = mockk(relaxed = true)

        every { database.solicitudDao() } returns solicitudDao
        every { solicitudDao.obtenerSolicitudes() } returns flowOf(emptyList())

        // Interceptamos AppDatabase para usar nuestro DAO simulado
        mockkObject(AppDatabase.Companion)
        every { AppDatabase.getDatabase(application) } returns database

        viewModel = SolicitudViewModel(application)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `guardarSolicitud inserta la solicitud en Room, limpia campos y activa guardadoExitoso`() = runTest {
        // Arrange (Llenamos los datos del formulario de solicitud)
        viewModel.ubicacion = "Fundo Santa Rosa, Curicó"
        viewModel.tipoCultivo = "Cerezos"
        viewModel.tipoProducto = "Fungicida"
        viewModel.superficie = "8 hás"
        viewModel.tieneAgua = true
        viewModel.aplicacion = "Nocturna"
        viewModel.fechaDisponibilidad = "30/11/2026"
        viewModel.toggleObstaculo("Antenas")
        viewModel.toggleObstaculo("Cables")
        viewModel.comentarios = "Sector norte"

        val solicitudCapturada = slot<Solicitud>()
        coEvery { solicitudDao.insertarSolicitud(capture(solicitudCapturada)) } returns Unit

        // Act
        viewModel.guardarSolicitud()
        advanceUntilIdle()

        // Assert
        // 1. Verifica que se insertó en el DAO
        coVerify(exactly = 1) { solicitudDao.insertarSolicitud(any()) }

        // 2. Verifica que los datos enviados al DAO coinciden con el formulario
        assertEquals("Fundo Santa Rosa, Curicó", solicitudCapturada.captured.ubicacion)
        assertEquals("Cerezos", solicitudCapturada.captured.tipoCultivo)
        assertEquals("Antenas, Cables", solicitudCapturada.captured.obstaculos)

        // 3. Verifica que se activó el estado de éxito y se limpió el formulario
        assertTrue(viewModel.guardadoExitoso)
        assertEquals("", viewModel.ubicacion)
        assertEquals("", viewModel.tipoCultivo)
    }
}