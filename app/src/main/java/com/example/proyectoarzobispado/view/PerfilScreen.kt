package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro

@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mi cuenta", style = MaterialTheme.typography.headlineMedium, color = TextoOscuro)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BordeSuave)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(64.dp).clip(CircleShape).background(Durazno),
                    contentAlignment = Alignment.Center
                ) {
                    Text("JP", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Terracota)
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    // TODO: reemplazar por los datos reales del usuario
                    Text("Juan Pérez", style = MaterialTheme.typography.titleMedium, color = TextoOscuro)
                    Text(
                        "juanperez@duocuc.cl",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoOscuro.copy(alpha = 0.7f)
                    )
                    Text("Turista", fontSize = 12.sp, color = Terracota)
                }
            }
        }

        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            border = BorderStroke(1.dp, BordeSuave)
        ) {
            Text("Cerrar sesión", color = Terracota)
        }
    }
}