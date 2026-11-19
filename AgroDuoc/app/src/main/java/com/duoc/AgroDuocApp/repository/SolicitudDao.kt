package com.duoc.AgroDuocApp.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duoc.AgroDuocApp.model.Solicitud
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {

    // En caso de que exista la solicitud la remplaza por la nueva
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarSolicitud(solicitud: Solicitud)

    // Sirve para luego mostrarlo en la pantalla Home (donde estan las solicitudes)
    @Query("SELECT * FROM solicitudes ORDER BY id DESC")
    fun obtenerSolicitudes(): Flow<List<Solicitud>>

}