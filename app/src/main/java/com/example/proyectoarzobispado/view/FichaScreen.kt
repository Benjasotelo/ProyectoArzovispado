package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.model.Evento
import com.example.proyectoarzobispado.model.GaleriaEjemplo
import com.example.proyectoarzobispado.model.ItemGaleria
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.FichaViewModel

@Composable
fun FichaScreen(onVolver: () -> Unit, vm: FichaViewModel = viewModel()) {
    val recinto = vm.recinto

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado: volver + título + corazón de favorito
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoOscuro
                )
            }
            Text(
                "Ficha patrimonial",
                style = MaterialTheme.typography.titleMedium,
                color = TextoOscuro
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { vm.alternarFavorito() }) {
                Icon(
                    imageVector = if (vm.esFavorito) Icons.Filled.Favorite
                    else Icons.Filled.FavoriteBorder,
                    contentDescription = if (vm.esFavorito) "Quitar de favoritos"
                    else "Guardar en favoritos",
                    tint = Terracota
                )
            }
        }

        if (recinto == null) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Terracota)
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BordeSuave)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            recinto.tipoRecinto,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Terracota
                        )
                        EstadoTag(recinto.estado)
                    }
                    Text(
                        recinto.nombreOficial,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextoOscuro
                    )
                    Text(
                        recinto.direccion,
                        fontSize = 14.sp,
                        color = TextoOscuro.copy(alpha = 0.7f)
                    )
                }
            }

            SeccionFicha("Historia y descripción", recinto.descripcionHistorica)
            SeccionFicha(
                "Horarios",
                if (recinto.horario.isBlank()) "Aún sin información."
                else recinto.horario
            )
            SeccionEventos(vm.eventos)
            SeccionGaleria(recinto.id)
            SeccionFicha(
                "Avisos",
                if (vm.avisos.isEmpty()) "Sin avisos por ahora."
                else vm.avisos.joinToString("\n\n") { "• ${it.titulo}: ${it.mensaje}" }
            )
        }

        // espacio para que la barra flotante no tape el contenido
        Spacer(Modifier.height(110.dp))
    }
}

@Composable
private fun SeccionFicha(titulo: String, texto: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Terracota
            )
            Text(texto, fontSize = 14.sp, color = TextoOscuro)
        }
    }
}

@Composable
private fun SeccionEventos(eventos: List<Evento>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "Eventos",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Terracota
            )
            if (eventos.isEmpty()) {
                Text("Aún sin información.", fontSize = 14.sp, color = TextoOscuro)
            } else {
                eventos.forEach { evento ->
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                evento.titulo,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextoOscuro,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "${evento.fecha} · ${evento.hora}",
                                fontSize = 12.sp,
                                color = Terracota
                            )
                        }
                        Text(
                            evento.descripcion,
                            fontSize = 14.sp,
                            color = TextoOscuro.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SeccionGaleria(recintoId: Int) {
    val items = GaleriaEjemplo.porRecinto(recintoId)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "Galería de imágenes",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Terracota
            )
            if (items.isEmpty()) {
                Text("Aún sin información.", fontSize = 14.sp, color = TextoOscuro)
            } else {
                // Fila de tarjetas que se desplaza en horizontal
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    items.forEach { item -> TarjetaGaleria(item) }
                }
            }
        }
    }
}

@Composable
private fun TarjetaGaleria(item: ItemGaleria) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.width(120.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Durazno),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icono,
                contentDescription = item.titulo,
                tint = Terracota,
                modifier = Modifier.size(34.dp)
            )
        }
        Text(
            item.titulo,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextoOscuro,
            maxLines = 1
        )
        Text(
            item.leyenda,
            fontSize = 11.sp,
            color = TextoOscuro.copy(alpha = 0.7f),
            maxLines = 2
        )
    }
}