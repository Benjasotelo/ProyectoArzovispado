package com.example.proyectoarzobispado.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.proyectoarzobispado.model.EstadoRecinto
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.model.Rol
import com.example.proyectoarzobispado.model.Usuario

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