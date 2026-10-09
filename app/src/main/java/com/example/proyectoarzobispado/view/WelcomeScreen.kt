package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoarzobispado.ui.theme.*

@Composable
fun WelcomeScreen(onRolSeleccionado: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Durazno.copy(alpha = 0.6f), Crema)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Spacer(Modifier.height(48.dp))
            LogoApp(72.dp)
            Spacer(Modifier.height(20.dp))
            Text("Red Patrimonial", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
            Spacer(Modifier.height(8.dp))
            Text("Descubre el patrimonio vivo de Santiago", fontSize = 16.sp, color = TextoOscuro.copy(alpha = 0.8f))

            Spacer(Modifier.weight(1f))

            Text("¿Cómo quieres ingresar?", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
            Spacer(Modifier.height(12.dp))

            RolCard("Turista", "Descubre templos y crea rutas", Icons.Filled.Explore) { onRolSeleccionado("Turista") }
            Spacer(Modifier.height(12.dp))
            RolCard("Colegio / Delegación", "Organiza una visita pedagógica", Icons.Filled.School) { onRolSeleccionado("Colegio / Delegación") }
            Spacer(Modifier.height(12.dp))
            RolCard("Encargado de Templo", "Gestiona tu comunidad", Icons.Filled.Business) { onRolSeleccionado("Encargado de Templo") }

            Spacer(Modifier.height(16.dp))
            Text(
                "Arzobispado de Santiago · Patrimonio cultural",
                fontSize = 11.sp,
                color = TextoOscuro.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RolCard(titulo: String, descripcion: String, icono: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BordeSuave),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).background(Durazno, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = Terracota)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
                Text(descripcion, fontSize = 12.sp, color = TextoOscuro.copy(alpha = 0.7f))
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = TextoOscuro)
        }
    }
}

