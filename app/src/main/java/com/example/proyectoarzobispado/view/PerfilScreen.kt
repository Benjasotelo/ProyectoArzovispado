package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit, vm: PerfilViewModel = viewModel()) {
    val usuario = vm.usuario

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
                    if (usuario != null) {
                        Text(usuario.iniciales, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Terracota)
                    } else {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Terracota)
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        usuario?.nombre ?: "Invitado",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextoOscuro
                    )
                    Text(
                        usuario?.email ?: "Inicia sesión para guardar tus rutas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoOscuro.copy(alpha = 0.7f)
                    )
                    usuario?.let { Text(it.rol.etiqueta, fontSize = 12.sp, color = Terracota) }
                }
            }
        }

        OutlinedButton(
            onClick = {
                vm.cerrarSesion()
                onCerrarSesion()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            border = BorderStroke(1.dp, BordeSuave)
        ) {
            Text(if (usuario != null) "Cerrar sesión" else "Iniciar sesión", color = Terracota)
        }
    }
}