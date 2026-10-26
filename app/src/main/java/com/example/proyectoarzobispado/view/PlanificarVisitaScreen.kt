package com.example.proyectoarzobispado.view

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.PlanificarVisitaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanificarVisitaScreen(
    onVolver: () -> Unit,
    vm: PlanificarVisitaViewModel = viewModel()
) {
    var mostrarFecha by remember { mutableStateOf(false) }
    var mostrarHora by remember { mutableStateOf(false) }
    val formato = remember {
        SimpleDateFormat("EEE d MMM yyyy", Locale("es", "CL"))
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
    }

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        // Barra superior
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = TextoOscuro)
            }
            Text("Planificar visita", style = MaterialTheme.typography.titleMedium, color = TextoOscuro)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Templo a visitar", fontWeight = FontWeight.SemiBold, color = TextoOscuro)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                vm.recintos.forEach { r ->
                    FilterChip(
                        selected = vm.recintoId == r.id,
                        onClick = { vm.elegirRecinto(r.id) },
                        label = { Text(r.nombreOficial, maxLines = 1) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Durazno,
                            selectedLabelColor = Terracota
                        )
                    )
                }
            }

            Text("Fecha y hora", fontWeight = FontWeight.SemiBold, color = TextoOscuro)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { mostrarFecha = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BordeSuave)
                ) {
                    Text(
                        vm.fechaMillis?.let { formato.format(Date(it)) } ?: "Elegir fecha",
                        color = Terracota
                    )
                }
                OutlinedButton(
                    onClick = { mostrarHora = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BordeSuave)
                ) {
                    Text(vm.hora ?: "Elegir hora", color = Terracota)
                }
            }

            OutlinedTextField(
                value = vm.participantes,
                onValueChange = vm::cambiarParticipantes,
                label = { Text("Número de participantes") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Solicitar guía voluntario", color = TextoOscuro)
                    Text(
                        "Un guía acompañará al grupo durante la visita.",
                        fontSize = 12.sp,
                        color = TextoOscuro.copy(alpha = 0.7f)
                    )
                }
                Switch(
                    checked = vm.requiereGuia,
                    onCheckedChange = vm::cambiarGuia,
                    colors = SwitchDefaults.colors(checkedTrackColor = Terracota)
                )
            }
        }

        Button(
            onClick = { vm.guardar(onListo = onVolver) },
            enabled = vm.puedeGuardar,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Terracota),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(if (vm.puedeGuardar) "Agendar visita" else "Completa todos los datos")
        }
    }

    if (mostrarFecha) {
        val estado = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { mostrarFecha = false },
            confirmButton = {
                TextButton(onClick = {
                    estado.selectedDateMillis?.let { vm.elegirFecha(it) }
                    mostrarFecha = false
                }) { Text("Aceptar", color = Terracota) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarFecha = false }) { Text("Cancelar", color = TextoOscuro) }
            }
        ) {
            DatePicker(
                state = estado,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Terracota,
                    todayDateBorderColor = Terracota,
                    todayContentColor = Terracota
                )
            )
        }
    }

    if (mostrarHora) {
        val estado = rememberTimePickerState(initialHour = 10, initialMinute = 0, is24Hour = true)
        AlertDialog(
            onDismissRequest = { mostrarHora = false },
            title = { Text("Hora de la visita") },
            text = {
                TimePicker(
                    state = estado,
                    colors = TimePickerDefaults.colors(
                        selectorColor = Terracota,
                        timeSelectorSelectedContainerColor = Durazno,
                        timeSelectorSelectedContentColor = Terracota
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.elegirHora("%02d:%02d".format(estado.hour, estado.minute))
                    mostrarHora = false
                }) { Text("Aceptar", color = Terracota) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarHora = false }) { Text("Cancelar", color = TextoOscuro) }
            }
        )
    }
}