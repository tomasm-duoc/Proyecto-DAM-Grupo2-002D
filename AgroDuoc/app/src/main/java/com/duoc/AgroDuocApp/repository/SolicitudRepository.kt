package com.duoc.AgroDuocApp.repository

import com.duoc.AgroDuocApp.model.Solicitud
import kotlinx.coroutines.flow.Flow

class SolicitudRepository(
    private val solicitudDao: SolicitudDao
) {
    val todasLasSolicitudes: Flow<List<Solicitud>> = solicitudDao.obtenerSolicitudes()

    suspend fun guardarSolicitud(solicitud: Solicitud){
        solicitudDao.insertarSolicitud(solicitud)
    }
}