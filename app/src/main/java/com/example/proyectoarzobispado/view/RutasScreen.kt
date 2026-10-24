package com.example.proyectoarzobispado.view

import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun RutasScreen(
    onCrearRuta: () -> Unit = {},
    onVerEnMapa: (Ruta) -> Unit = {},
    onEditarRuta: (Ruta) -> Unit = {},
    onRutaEliminada: (Long) -> Unit = {},
    vm: RutasViewModel = viewModel()
) {
    val rutas = vm.rutas

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Rutas patrimoniales",
                style = MaterialTheme.typography.headlineSmall,
                color = TextoOscuro,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            if (rutas.isEmpty()) {
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
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 140.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                     items(rutas, key = { it.id }) { ruta ->
                        RutaCard(
                            ruta = ruta,
                            onClick = { onVerEnMapa(ruta) },
                            onEditar = { onEditarRuta(ruta) },
                            onEliminar = {
                                vm.eliminarRuta(ruta.id)
                                onRutaEliminada(ruta.id)
                            }                        )
                    }
                }
            }
        }

        // Botón flotante para crear otra ruta (solo si ya hay alguna)
        if (rutas.isNotEmpty()) {
            FloatingActionButton(
                onClick = onCrearRuta,
                containerColor = Terracota,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 110.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Crear ruta")
            }
        }
    }
}
@Composable
private fun RutaCard(
    ruta: Ruta,
    onClick: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
)  {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = ruta.nombre,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoOscuro
                )
                Text(
                    text = "${ruta.paradas.size} paradas · ~${ruta.tiempoTotalMinutos} min",
                    fontSize = 13.sp,
                    color = TextoOscuro.copy(alpha = 0.7f)
                )
                Text(
                    text = "Toca para ver en el mapa",
                    fontSize = 12.sp,
                    color = Terracota
                )
            }
            Row {
                IconButton(onClick = onEditar) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar ruta",
                        tint = Terracota
                    )
                }
                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar ruta",
                        tint = Color(0xFF8A2A22)
                    )
                }
            }
        }
    }

}