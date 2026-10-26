package com.example.proyectoarzobispado.model

enum class EstadoVisita(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada")
}

data class Visita(
    val id: Long,
    val recintoId: Int,
    val fechaMillis: Long,      // fecha elegida en el calendario
    val hora: String,           // "10:30"
    val participantes: Int,
    val requiereGuia: Boolean,
    val estado: EstadoVisita
)