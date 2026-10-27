package com.example.proyectoarzobispado.model

data class Evento(
    val id: Long = 0,
    val recintoId: Int,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val hora: String
)

object EventosEjemplo {
    val lista = listOf(
        Evento(recintoId = 1, titulo = "Visita guiada patrimonial",
            descripcion = "Recorrido por la nave central, el altar mayor y la cripta con un guía del Área de Cultura.",
            fecha = "5 de noviembre", hora = "11:00"),
        Evento(recintoId = 1, titulo = "Concierto de coro universitario",
            descripcion = "Presentación gratuita en el atrio de la Catedral.",
            fecha = "9 de noviembre", hora = "19:30"),
        Evento(recintoId = 2, titulo = "Taller de restauración",
            descripcion = "Charla abierta sobre la restauración del convento franciscano.",
            fecha = "8 de noviembre", hora = "15:00"),
        Evento(recintoId = 3, titulo = "Recorrido por el museo",
            descripcion = "Visita al museo anexo con piezas coloniales y religiosas.",
            fecha = "12 de noviembre", hora = "16:00"),
        Evento(recintoId = 5, titulo = "Concierto de canto gregoriano",
            descripcion = "Ensamble vocal en la nave principal del santuario.",
            fecha = "14 de noviembre", hora = "18:00")
    )
}