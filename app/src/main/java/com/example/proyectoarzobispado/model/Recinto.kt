package com.example.proyectoarzobispado.model

enum class EstadoRecinto(val etiqueta: String) {
    ABIERTO("Abierto"),
    CERRADO("Cerrado"),
    EN_CELEBRACION("En celebración")
}

data class Recinto(
    val id: Int,
    val nombreOficial: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val descripcionHistorica: String,
    val tipoRecinto: String,
    val estado: EstadoRecinto
)

object RecintosEjemplo {
    val lista = listOf(
        Recinto(
            1, "Catedral Metropolitana de Santiago", "Plaza de Armas s/n, Santiago",
            -33.4372, -70.6506,
            "Sede de la Arquidiócesis de Santiago. Su construcción comenzó en el siglo XVIII.",
            "Catedral", EstadoRecinto.ABIERTO
        ),
        Recinto(
            2, "Iglesia de San Francisco", "Av. Libertador Bernardo O'Higgins 834, Santiago",
            -33.4443, -70.6490,
            "Uno de los templos más antiguos de Chile, junto al convento franciscano.",
            "Iglesia", EstadoRecinto.EN_CELEBRACION
        ),
        Recinto(
            3, "Basílica de la Merced", "Merced 341, Santiago",
            -33.4392, -70.6486,
            "Templo mercedario de origen colonial, con museo anexo.",
            "Basílica", EstadoRecinto.ABIERTO
        ),
        Recinto(
            4, "Parroquia Santa Ana", "Av. Santa María 2, Santiago",
            -33.4350, -70.6590,
            "Parroquia patrimonial ubicada fuera del circuito de la Plaza de Armas.",
            "Parroquia", EstadoRecinto.CERRADO
        ),
        Recinto(
            5, "Basílica de Lourdes", "Compañía de Jesús 2, Quinta Normal",
            -33.4415, -70.6858,
            "Santuario de estilo neogótico en la comuna de Quinta Normal.",
            "Basílica", EstadoRecinto.ABIERTO
        )
    )
}