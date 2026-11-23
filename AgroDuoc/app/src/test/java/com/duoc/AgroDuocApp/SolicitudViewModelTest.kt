package com.duoc.AgroDuocApp

import android.app.Application
import com.duoc.AgroDuocApp.model.SolicitudDto
import com.duoc.AgroDuocApp.repository.AppDatabase
import com.duoc.AgroDuocApp.repository.SolicitudDao
import com.duoc.AgroDuocApp.repository.XanoDataRepository
import com.duoc.AgroDuocApp.viewmodel.SolicitudViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
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

        // Interceptamos AppDatabase
        mockkObject(AppDatabase.Companion)
        every { AppDatabase.getDatabase(application) } returns database

        // Interceptamos cualquier instancia creada de XanoDataRepository
        mockkConstructor(XanoDataRepository::class)
        coEvery { anyConstructed<XanoDataRepository>().obtenerSolicitudesDeProductorActual() } returns Pair(null, emptyList())

        viewModel = SolicitudViewModel(application)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `guardarSolicitud envia datos a Xano, limpia campos y activa guardadoExitoso`() = runTest {
        // Arrange (Llenamos los datos del formulario de solicitud)
        viewModel.ubicacion = "Fundo Santa Rosa, Curicó"
        viewModel.tipoCultivo = "Cerezos"
        viewModel.tipoProducto = "Fungicida"
        viewModel.superficie = "8,5"
        viewModel.tieneAgua = true
        viewModel.aplicacion = "Nocturna"
        viewModel.toggleObstaculo("Antenas")
        viewModel.toggleObstaculo("Cables")
        viewModel.comentarios = "Sector norte"

        val superficieCapturada = slot<Double>()
        val productoCapturado = slot<String>()
        val comentariosCapturados = slot<String>()

        coEvery {
            anyConstructed<XanoDataRepository>().crearSolicitudXano(
                superficieHa = capture(superficieCapturada),
                tipoProducto = capture(productoCapturado),
                tieneAgua = any(),
                tipoAplicacion = any(),
                fotos = any(),
                comentarios = capture(comentariosCapturados)
            )
        } returns mockk<SolicitudDto>(relaxed = true)

        // Act
        viewModel.guardarSolicitud()
        advanceUntilIdle()

        // Assert
        // 1. Verifica que se llamó a crearSolicitudXano 1 vez
        coVerify(exactly = 1) {
            anyConstructed<XanoDataRepository>().crearSolicitudXano(
                superficieHa = any(),
                tipoProducto = any(),
                tieneAgua = any(),
                tipoAplicacion = any(),
                fotos = any(),
                comentarios = any()
            )
        }

        // 2. Verifica que el ViewModel transformó los datos correctamente antes de enviarlos a Xano
        assertEquals(8.5, superficieCapturada.captured, 0.01)
        assertEquals("Fungicida", productoCapturado.captured)
        assertTrue(comentariosCapturados.captured.contains("Cultivo: Cerezos"))
        assertTrue(comentariosCapturados.captured.contains("Obstáculos: Antenas, Cables"))

        // 3. Verifica que se activó el estado de éxito (para la notificación) y se limpió el formulario
        assertTrue(viewModel.guardadoExitoso)
        assertEquals("", viewModel.ubicacion)
        assertEquals("", viewModel.tipoCultivo)
    }
}