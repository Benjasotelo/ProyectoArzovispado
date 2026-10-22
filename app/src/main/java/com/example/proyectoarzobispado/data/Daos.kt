package com.example.proyectoarzobispado.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    // Devuelve -1 si el correo ya existía (se ignora el duplicado)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(usuario: UsuarioEntity): Long

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): UsuarioEntity?

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}

@Dao
interface RecintoDao {
    @Query("SELECT * FROM recintos ORDER BY id")
    fun observarTodos(): Flow<List<RecintoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodos(lista: List<RecintoEntity>)

    // Para que el Encargado cambie el estado (CU-18)
    @Query("UPDATE recintos SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)
}