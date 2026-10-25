package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.FichaViewModel
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder

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
        Row(
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
            Row(
                modifier = Modifier.fillMaxWidth(),          // <- agregar esta línea
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onVolver) { /* igual que antes */ }
                Text("Ficha patrimonial", /* igual que antes */)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { vm.alternarFavorito() }) {
                    Icon(
                        imageVector = if (vm.esFavorito) Icons.Filled.Favorite
                        else Icons.Filled.FavoriteBorder,
                        contentDescription = if (vm.esFavorito) "Quitar de favoritos" else "Guardar en favoritos",
                        tint = Terracota
                    )
                }
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
            SeccionFicha("Horarios", "Aún sin información.")
            SeccionFicha("Eventos", "Aún sin información.")
            SeccionFicha("Galería de imágenes", "Aún sin información.")
            SeccionFicha(
                "Avisos",
                if (vm.avisos.isEmpty()) "Sin avisos por ahora."
                else vm.avisos.joinToString("\n\n") { "• ${it.titulo}: ${it.mensaje}" }
            )        }

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