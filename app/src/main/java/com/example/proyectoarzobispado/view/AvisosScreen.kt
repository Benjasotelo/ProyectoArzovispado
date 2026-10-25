package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.AvisosViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AvisosScreen(vm: AvisosViewModel = viewModel()) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    val formato = remember { SimpleDateFormat("dd MMM, HH:mm", Locale("es", "CL")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Avisos",
                style = MaterialTheme.typography.headlineMedium,
                color = TextoOscuro,
                modifier = Modifier.weight(1f)
            )
            if (vm.esEncargado) {
                FilledTonalButton(
                    onClick = { mostrarDialogo = true },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Durazno, contentColor = Terracota
                    )
                ) {
                    Icon(Icons.Filled.Add, null)
                    Spacer(Modifier.width(4.dp))
                    Text("Publicar")
                }
            }
        }

        if (vm.avisos.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.Notifications, null, tint = Terracota.copy(alpha = 0.4f))
                Text("No hay avisos por ahora", fontWeight = FontWeight.SemiBold, color = TextoOscuro)
            }
        } else {
            vm.avisos.forEach { aviso ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BordeSuave)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                vm.nombreRecinto(aviso.recintoId),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Terracota
                            )
                            Text(aviso.titulo, fontWeight = FontWeight.SemiBold, color = TextoOscuro)
                            Text(aviso.mensaje, fontSize = 14.sp, color = TextoOscuro)
                            Text(
                                formato.format(Date(aviso.fecha)),
                                fontSize = 12.sp,
                                color = TextoOscuro.copy(alpha = 0.6f)
                            )
                        }
                        if (vm.esEncargado) {
                            IconButton(onClick = { vm.eliminar(aviso.id) }) {
                                Icon(Icons.Filled.Delete, "Eliminar aviso", tint = TextoOscuro.copy(alpha = 0.6f))
                            }
                        }
                    }
                }
            }
        }

        // espacio para que la barra flotante no tape el contenido
        Spacer(Modifier.height(110.dp))
    }

    if (mostrarDialogo) {
        PublicarAvisoDialog(
            recintos = vm.recintos,
            onCancelar = { mostrarDialogo = false },
            onPublicar = { recintoId, titulo, mensaje ->
                vm.publicar(recintoId, titulo, mensaje)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
private fun PublicarAvisoDialog(
    recintos: List<Recinto>,
    onCancelar: () -> Unit,
    onPublicar: (Int, String, String) -> Unit
) {
    var recintoId by remember { mutableStateOf(recintos.firstOrNull()?.id) }
    var titulo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    val puedePublicar = recintoId != null && titulo.isNotBlank() && mensaje.isNotBlank()

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Nuevo aviso") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Recinto", fontSize = 13.sp, color = TextoOscuro.copy(alpha = 0.7f))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    recintos.forEach { r ->
                        FilterChip(
                            selected = recintoId == r.id,
                            onClick = { recintoId = r.id },
                            label = { Text(r.nombreOficial, maxLines = 1) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Durazno,
                                selectedLabelColor = Terracota
                            )
                        )
                    }
                }
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = mensaje,
                    onValueChange = { mensaje = it },
                    label = { Text("Mensaje") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = puedePublicar,
                onClick = { onPublicar(recintoId!!, titulo, mensaje) }
            ) { Text("Publicar", color = if (puedePublicar) Terracota else Color.Gray) }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar", color = TextoOscuro) }
        }
    )
}