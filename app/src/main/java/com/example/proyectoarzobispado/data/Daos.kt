package com.example.proyectoarzobispado.data

import androidx.room.Update
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction

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

    @Query("UPDATE usuarios SET salt = :salt, passwordHash = :hash WHERE id = :id")
    suspend fun cambiarClave(id: Long, salt: String, hash: String)
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

    // Para que el Encargado edite la ficha
    @Update
    suspend fun actualizarFicha(recinto: RecintoEntity)
}

@Dao
interface RutaDao {
    @Transaction
    @Query("SELECT * FROM rutas ORDER BY id DESC")
    fun observarTodas(): Flow<List<RutaConParadas>>

    // REPLACE: si el id ya existe (edición), reemplaza la ruta
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarRuta(ruta: RutaEntity)

    @Insert
    suspend fun insertarParadas(paradas: List<ParadaEntity>)

    @Query("DELETE FROM paradas_ruta WHERE rutaId = :rutaId")
    suspend fun borrarParadas(rutaId: Long)

    @Query("DELETE FROM rutas WHERE id = :id")
    suspend fun eliminar(id: Long)
}

@Dao
interface FavoritoDao {
    @Query("SELECT recintoId FROM favoritos WHERE usuarioId = :usuarioId")
    fun observarIds(usuarioId: Long): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun agregar(favorito: FavoritoEntity)

    @Query("DELETE FROM favoritos WHERE usuarioId = :usuarioId AND recintoId = :recintoId")
    suspend fun quitar(usuarioId: Long, recintoId: Int)
}

@Dao
interface AvisoDao {
    @Query("SELECT * FROM avisos ORDER BY fecha DESC")
    fun observarTodos(): Flow<List<AvisoEntity>>

    @Insert
    suspend fun insertar(aviso: AvisoEntity)

    @Query("DELETE FROM avisos WHERE id = :id")
    suspend fun eliminar(id: Long)

    @Query("SELECT COUNT(*) FROM avisos")
    suspend fun contar(): Int
}

@Dao
interface VisitaDao {
    // Cada colegio ve solo sus visitas, ordenadas por fecha
    @Query("SELECT * FROM visitas WHERE usuarioId = :usuarioId ORDER BY fechaMillis")
    fun observarDe(usuarioId: Long): Flow<List<VisitaEntity>>

    @Insert
    suspend fun insertar(visita: VisitaEntity)

    @Query("UPDATE visitas SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(id: Long, estado: String)

    @Query("DELETE FROM visitas WHERE id = :id")
    suspend fun eliminar(id: Long)
}

@Dao
interface EventoDao {
    // Eventos de un recinto, para la ficha
    @Query("SELECT * FROM eventos WHERE recintoId = :recintoId ORDER BY id")
    fun observarDe(recintoId: Int): Flow<List<EventoEntity>>

    // Todos los eventos, para "Mi recinto" del Encargado
    @Query("SELECT * FROM eventos ORDER BY id")
    fun observarTodos(): Flow<List<EventoEntity>>

    @Insert
    suspend fun insertar(evento: EventoEntity)

    @Insert
    suspend fun insertarTodos(lista: List<EventoEntity>)

    @Query("DELETE FROM eventos WHERE id = :id")
    suspend fun eliminar(id: Long)

    @Query("SELECT COUNT(*) FROM eventos")
    suspend fun contar(): Int
}