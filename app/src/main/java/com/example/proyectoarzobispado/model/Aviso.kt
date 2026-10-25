package com.example.proyectoarzobispado.model

data class Aviso(
    val id: Long,
    val recintoId: Int,
    val titulo: String,
    val mensaje: String,
    val fecha: Long   // milisegundos desde 1970, se formatea al mostrarla
)