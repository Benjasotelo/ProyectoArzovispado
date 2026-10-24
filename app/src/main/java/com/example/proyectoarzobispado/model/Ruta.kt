package com.example.proyectoarzobispado.model

/**
 * Una parada dentro de una ruta.
 * orden: 1 = primera parada, 2 = segunda, etc.
 * tiempoEstimadoMinutos: minutos caminando desde la parada anterior (0 en la primera).
 */
data class ParadaRuta(
    val recintoId: Int,
    val orden: Int,
    val tiempoEstimadoMinutos: Int = 0
)

/**
 * Ruta patrimonial creada por el usuario.
 * Por ahora vive solo en memoria; más adelante la guardaremos en Room.
 */
data class Ruta(
    val id: Long,
    val nombre: String,
    val paradas: List<ParadaRuta>,
    val tiempoTotalMinutos: Int
)