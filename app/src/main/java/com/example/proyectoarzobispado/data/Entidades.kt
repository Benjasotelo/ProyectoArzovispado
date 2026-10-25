package com.example.proyectoarzobispado.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.proyectoarzobispado.model.EstadoRecinto
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.model.Rol
import com.example.proyectoarzobispado.model.Usuario
import androidx.room.Embedded
import androidx.room.ForeignKey
import androidx.room.Relation
import com.example.proyectoarzobispado.model.ParadaRuta
import com.example.proyectoarzobispado.model.Ruta
import com.example.proyectoarzobispado.model.Aviso

@Entity(tableName = "usuarios", indices = [Index(value = ["email"], unique = true)])
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val email: String,
    val salt: String,
    val passwordHash: String,
    val rol: String,
    val activo: Boolean = true
)

@Entity(tableName = "recintos")
data class RecintoEntity(
    @PrimaryKey val id: Int,
    val nombreOficial: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val descripcionHistorica: String,
    val tipoRecinto: String,
    val estado: String
)

fun UsuarioEntity.aModelo() = Usuario(id, nombre, email, Rol.valueOf(rol))

fun RecintoEntity.aModelo() = Recinto(
    id, nombreOficial, direccion, latitud, longitud,
    descripcionHistorica, tipoRecinto, EstadoRecinto.valueOf(estado)
)

fun Recinto.aEntidad() = RecintoEntity(
    id, nombreOficial, direccion, latitud, longitud,
    descripcionHistorica, tipoRecinto, estado.name
)

@Entity(tableName = "rutas")
data class RutaEntity(
    @PrimaryKey val id: Long,
    val nombre: String,
    val tiempoTotalMinutos: Int
)

// Si se borra la ruta, Room borra sus paradas solo (CASCADE)
@Entity(
    tableName = "paradas_ruta",
    foreignKeys = [ForeignKey(
        entity = RutaEntity::class,
        parentColumns = ["id"],
        childColumns = ["rutaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("rutaId")]
)

data class ParadaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rutaId: Long,
    val recintoId: Int,
    val orden: Int,
    val tiempoEstimadoMinutos: Int
)

// Una ruta con su lista de paradas, leída en una sola consulta
data class RutaConParadas(
    @Embedded val ruta: RutaEntity,
    @Relation(parentColumn = "id", entityColumn = "rutaId")
    val paradas: List<ParadaEntity>
)

fun RutaConParadas.aModelo() = Ruta(
    id = ruta.id,
    nombre = ruta.nombre,
    paradas = paradas.sortedBy { it.orden }
        .map { ParadaRuta(it.recintoId, it.orden, it.tiempoEstimadoMinutos) },
    tiempoTotalMinutos = ruta.tiempoTotalMinutos
)

// Un favorito = "este usuario guardó este recinto". La clave es la pareja, así no se repite.
@Entity(tableName = "favoritos", primaryKeys = ["usuarioId", "recintoId"])
data class FavoritoEntity(
    val usuarioId: Long,
    val recintoId: Int
)

@Entity(tableName = "avisos")
data class AvisoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recintoId: Int,
    val titulo: String,
    val mensaje: String,
    val fecha: Long
)

fun AvisoEntity.aModelo() = Aviso(id, recintoId, titulo, mensaje, fecha)