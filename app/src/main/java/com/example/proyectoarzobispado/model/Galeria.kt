package com.example.proyectoarzobispado.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.ui.graphics.vector.ImageVector

data class ItemGaleria(
    val titulo: String,
    val leyenda: String,
    val icono: ImageVector
)

object GaleriaEjemplo {
    private val porRecinto = mapOf(
        1 to listOf(
            ItemGaleria("Altar mayor", "Retablo dorado del siglo XIX", Icons.Filled.Church),
            ItemGaleria("Órgano de tubos", "Instrumento original de 1905", Icons.Filled.MusicNote),
            ItemGaleria("Cripta", "Restos de los primeros obispos", Icons.Filled.AccountBalance)
        ),
        2 to listOf(
            ItemGaleria("Claustro", "Patio colonial del convento", Icons.Filled.Architecture),
            ItemGaleria("Museo franciscano", "Piezas de arte sacro", Icons.Filled.Museum),
            ItemGaleria("Torre", "Vista desde la fachada", Icons.Filled.Image)
        ),
        3 to listOf(
            ItemGaleria("Museo anexo", "Arte colonial y religioso", Icons.Filled.Museum),
            ItemGaleria("Retablo mayor", "Talla de madera dorada", Icons.Filled.Church),
            ItemGaleria("Biblioteca", "Archivo histórico mercedario", Icons.Filled.AutoStories)
        ),
        4 to listOf(
            ItemGaleria("Fachada", "Frente de la parroquia", Icons.Filled.Image),
            ItemGaleria("Interior", "Nave principal restaurada", Icons.Filled.Church),
            ItemGaleria("Campanario", "Estructura original", Icons.Filled.Architecture)
        ),
        5 to listOf(
            ItemGaleria("Gruta", "Réplica de la Gruta de Lourdes", Icons.Filled.Landscape),
            ItemGaleria("Vitrales", "Vidrios neogóticos importados", Icons.Filled.Image),
            ItemGaleria("Jardín", "Área verde del santuario", Icons.Filled.LocalFlorist)
        )
    )

    fun porRecinto(recintoId: Int): List<ItemGaleria> = porRecinto[recintoId] ?: emptyList()
}