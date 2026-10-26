package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.model.EstadoVisita
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.VisitasViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun VisitasScreen(onPlanificar: () -> Unit, vm: VisitasViewModel = viewModel()) {
    // El calendario entrega la fecha en UTC, por eso se formatea en UTC
    val formato = remember {
        SimpleDateFormat("EEE d MMM yyyy", Locale("es", "CL"))
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Visitas",
                style = MaterialTheme.typography.headlineMedium,
                color = TextoOscuro,
                modifier = Modifier.weight(1f)
            )
            FilledTonalButton(
                onClick = onPlanificar,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Durazno, contentColor = Terracota
                )
            ) {
                Icon(Icons.Filled.Add, null)
                Spacer(Modifier.width(4.dp))
                Text("Planificar")
            }
        }

        if (vm.visitas.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.CalendarMonth, null, tint = Terracota.copy(alpha = 0.4f))
                Text("Aún no tienes visitas", fontWeight = FontWeight.SemiBold, color = TextoOscuro)
                Text(
                    "Planifica la visita de tu colegio a un templo.",
                    fontSize = 14.sp,
                    color = TextoOscuro.copy(alpha = 0.7f)
                )
            }
        } else {
            vm.visitas.forEach { visita ->
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                vm.nombreRecinto(visita.recintoId),
                                fontWeight = FontWeight.SemiBold,
                                color = TextoOscuro,
                                modifier = Modifier.weight(1f)
                            )
                            EtiquetaVisita(visita.estado)
                        }
                        Text(
                            "${formato.format(Date(visita.fechaMillis))} · ${visita.hora}",
                            fontSize = 14.sp,
                            color = TextoOscuro
                        )
                        Text(
                            "${visita.participantes} participantes · " +
                                    if (visita.requiereGuia) "Con guía" else "Sin guía",
                            fontSize = 13.sp,
                            color = TextoOscuro.copy(alpha = 0.7f)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (visita.estado == EstadoVisita.PENDIENTE) {
                                TextButton(onClick = { vm.confirmar(visita.id) }) {
                                    Text("Confirmar", color = Terracota)
                                }
                            }
                            IconButton(onClick = { vm.eliminar(visita.id) }) {
                                Icon(
                                    Icons.Filled.Delete, "Cancelar visita",
                                    tint = TextoOscuro.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // espacio para que la barra flotante no tape el contenido
        Spacer(Modifier.height(110.dp))
    }
}

@Composable
private fun EtiquetaVisita(estado: EstadoVisita) {
    val (fondo, texto) = when (estado) {
        EstadoVisita.PENDIENTE -> Color(0xFFFBE7B8) to Color(0xFF7A5200)
        EstadoVisita.CONFIRMADA -> Color(0xFFCFEFD6) to Color(0xFF1E5E34)
    }
    Text(
        estado.etiqueta,
        fontSize = 12.sp,
        color = texto,
        modifier = Modifier
            .clip(CircleShape)
            .background(fondo)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}