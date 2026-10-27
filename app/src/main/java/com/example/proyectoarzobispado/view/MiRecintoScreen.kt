package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.model.EstadoRecinto
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.MiRecintoViewModel

@Composable
fun MiRecintoScreen(vm: MiRecintoViewModel = viewModel()) {
    val seleccionado = vm.recintoSeleccionado

    // Campos del formulario "Nuevo evento"
    var titulo by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mi recinto", style = MaterialTheme.typography.headlineMedium, color = TextoOscuro)
        Text(
            "Elige un recinto y actualiza su estado.",
            fontSize = 14.sp,
            color = TextoOscuro.copy(alpha = 0.7f)
        )

        vm.recintos.forEach { recinto ->
            val esSeleccionado = recinto.id == seleccionado?.id
            Card(
                onClick = { vm.seleccionarRecinto(recinto.id) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (esSeleccionado) Durazno else Color.White
                ),
                border = BorderStroke(
                    width = if (esSeleccionado) 2.dp else 1.dp,
                    color = if (esSeleccionado) Terracota else BordeSuave
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            recinto.nombreOficial,
                            fontWeight = FontWeight.SemiBold,
                            color = TextoOscuro
                        )
                        Text(
                            recinto.direccion,
                            fontSize = 13.sp,
                            color = TextoOscuro.copy(alpha = 0.7f)
                        )
                    }
                    EstadoTag(recinto.estado)
                }
            }
        }

        if (seleccionado != null) {
            Text(
                "Estado de ${seleccionado.nombreOficial}",
                style = MaterialTheme.typography.titleMedium,
                color = TextoOscuro
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EstadoRecinto.entries.forEach { estado ->
                    ChipEstado(
                        estado = estado,
                        seleccionado = seleccionado.estado == estado,
                        onClick = { vm.cambiarEstado(estado) }
                    )
                }
            }
            Text(
                "El cambio se guarda al instante y se ve en el mapa.",
                fontSize = 13.sp,
                color = TextoOscuro.copy(alpha = 0.7f)
            )
        }

        if (seleccionado != null) {
            Text(
                "Eventos de ${seleccionado.nombreOficial}",
                style = MaterialTheme.typography.titleMedium,
                color = TextoOscuro
            )

            // Lista de eventos publicados del recinto elegido
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
                    if (vm.eventosSeleccionado.isEmpty()) {
                        Text(
                            "Sin eventos publicados.",
                            fontSize = 13.sp,
                            color = TextoOscuro.copy(alpha = 0.6f)
                        )
                    }
                    vm.eventosSeleccionado.forEach { evento ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    evento.titulo,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = TextoOscuro
                                )
                                Text(
                                    "${evento.fecha} · ${evento.hora}",
                                    fontSize = 12.sp,
                                    color = Terracota
                                )
                                Text(
                                    evento.descripcion,
                                    fontSize = 13.sp,
                                    color = TextoOscuro.copy(alpha = 0.8f)
                                )
                            }
                            IconButton(onClick = { vm.eliminarEvento(evento.id) }) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Eliminar evento",
                                    tint = Terracota
                                )
                            }
                        }
                    }
                }
            }

            // Formulario para publicar un evento
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
                        "Nuevo evento",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Terracota
                    )
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = fecha,
                            onValueChange = { fecha = it },
                            label = { Text("Fecha") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = hora,
                            onValueChange = { hora = it },
                            label = { Text("Hora") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    FilledTonalButton(
                        onClick = {
                            vm.agregarEvento(titulo, fecha, hora, descripcion)
                            titulo = ""; fecha = ""; hora = ""; descripcion = ""
                        },
                        enabled = titulo.isNotBlank() && fecha.isNotBlank() && hora.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Durazno,
                            contentColor = Terracota
                        )
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Agregar evento")
                    }
                }
            }
        }

        // espacio para que la barra flotante no tape el contenido
        Spacer(Modifier.height(110.dp))
    }
}