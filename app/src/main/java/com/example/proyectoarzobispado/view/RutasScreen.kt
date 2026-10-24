package com.example.proyectoarzobispado.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.model.Ruta
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.RutasViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color

@Composable
fun RutasScreen(
    onCrearRuta: () -> Unit = {},
    onVerEnMapa: (Ruta) -> Unit = {},
    vm: RutasViewModel = viewModel()
){
    val rutas = vm.rutas

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Título
        Text(
            text = "Rutas patrimoniales",
            style = MaterialTheme.typography.headlineSmall,
            color = TextoOscuro,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        if (rutas.isEmpty()) {
            // Estado vacío
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Route,
                    contentDescription = null,
                    tint = Terracota,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Aún no tienes rutas",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoOscuro
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Crea un recorrido entre templos y guárdalo como favorito.",
                    fontSize = 14.sp,
                    color = TextoOscuro.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(28.dp))
                Button(
                    onClick = onCrearRuta,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracota),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Crear ruta")
                }
                Spacer(Modifier.height(100.dp))
            }
        } else {
            // Lista de rutas (cuando haya alguna)
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 120.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(rutas, key = { it.id }) { ruta ->
                    RutaCard(
                        ruta = ruta,
                        onClick = { onVerEnMapa(ruta) }
                    )
                }
            }
        }
    }

    // Botón flotante cuando ya hay rutas
    if (rutas.isNotEmpty()) {
        // Lo dejamos preparado; lo posicionaremos bien en el siguiente ajuste visual
    }
}

@Composable
private fun RutaCard(
    ruta: Ruta,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = ruta.nombre,
                fontWeight = FontWeight.SemiBold,
                color = TextoOscuro
            )
            Text(
                text = "${ruta.paradas.size} paradas · ~${ruta.tiempoTotalMinutos} min · Toca para ver en el mapa",
                fontSize = 13.sp,
                color = TextoOscuro.copy(alpha = 0.7f)
            )
        }
    }
}