package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.FavoritosViewModel

@Composable
fun FavoritosScreen(
    onVolver: () -> Unit,
    onAbrirFicha: (Int) -> Unit,
    vm: FavoritosViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = TextoOscuro)
            }
            Text("Mis favoritos", style = MaterialTheme.typography.titleMedium, color = TextoOscuro)
        }

        if (vm.favoritos.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.Favorite, null, tint = Terracota.copy(alpha = 0.4f))
                Text("Aún no tienes favoritos", fontWeight = FontWeight.SemiBold, color = TextoOscuro)
                Text(
                    "Toca el corazón en la ficha de un templo para guardarlo aquí.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = TextoOscuro.copy(alpha = 0.7f)
                )
            }
        } else {
            vm.favoritos.forEach { recinto ->
                Card(
                    onClick = { onAbrirFicha(recinto.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BordeSuave)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(recinto.nombreOficial, fontWeight = FontWeight.SemiBold, color = TextoOscuro)
                            Text(recinto.direccion, fontSize = 13.sp, color = TextoOscuro.copy(alpha = 0.7f))
                            EstadoTag(recinto.estado)
                        }
                        IconButton(onClick = { vm.quitar(recinto.id) }) {
                            Icon(Icons.Filled.Favorite, "Quitar de favoritos", tint = Terracota)
                        }
                    }
                }
            }
        }

        // espacio para que la barra flotante no tape el contenido
        Spacer(Modifier.height(110.dp))
    }
}