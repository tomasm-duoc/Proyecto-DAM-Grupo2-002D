package com.duoc.AgroDuocApp.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duoc.AgroDuocApp.model.User

@Dao
interface UserDao {
    // Inserta o reemplaza un usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // Valida el login buscando por email, rut o nombre junto con la contraseña
    @Query("SELECT * FROM users WHERE (email = :identifier OR rut = :identifier OR nombre = :identifier) AND password = :password LIMIT 1")
    suspend fun loginUser(identifier: String, password: String): User?

    // Busca un usuario por su RUT
    @Query("SELECT * FROM users WHERE rut = :rut LIMIT 1")
    suspend fun getUserByRut(rut: String): User?

    // Busca un usuario por su email
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}
