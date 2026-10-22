package com.example.proyectoarzobispado.model

enum class Rol(val etiqueta: String) {
    TURISTA("Turista"),
    COLEGIO("Colegio / Delegación"),
    ENCARGADO("Encargado de Templo")
}

data class Usuario(
    val id: Long,
    val nombre: String,
    val email: String,
    val rol: Rol
) {
    val iniciales: String
        get() = nombre.trim().split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
}