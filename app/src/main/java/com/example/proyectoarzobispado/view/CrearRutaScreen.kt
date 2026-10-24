package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.model.Ruta
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.CrearRutaViewModel

@Composable
fun CrearRutaScreen(
    onVolver: () -> Unit,
    onGuardar: (Ruta) -> Unit,
    vm: CrearRutaViewModel = viewModel()
) {
    val puedeGuardar = vm.idsSeleccionados.size >= 2

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Barra superior
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoOscuro
                )
            }
            Text(
                "Crear ruta",
                style = MaterialTheme.typography.titleMedium,
                color = TextoOscuro
            )
        }

        // Nombre de la ruta
        OutlinedTextField(
            value = vm.nombre,
            onValueChange = vm::actualizarNombre,
            label = { Text("Nombre de la ruta") },
            placeholder = { Text("Ej: Circuito centro histórico") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Text(
            text = "Elige al menos 2 recintos (en el orden del recorrido)",
            fontSize = 13.sp,
            color = TextoOscuro.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        // Lista de recintos
        LazyColumn(
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(vm.recintos, key = { it.id }) { recinto ->
                RecintoSeleccionCard(
                    recinto = recinto,
                    seleccionado = vm.estaSeleccionado(recinto.id),
                    orden = vm.idsSeleccionados.indexOf(recinto.id).let {
                        if (it >= 0) it + 1 else null
                    },
                    onClick = { vm.alternarRecinto(recinto.id) }
                )
            }
        }

        // Botón guardar
        Button(
            onClick = {
                vm.construirRuta()?.let { onGuardar(it) }
            },
            enabled = puedeGuardar,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Terracota),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                if (puedeGuardar) "Guardar ruta (${vm.idsSeleccionados.size} paradas)"
                else "Elige al menos 2 recintos"
            )
        }
    }
}

@Composable
private fun RecintoSeleccionCard(
    recinto: Recinto,
    seleccionado: Boolean,
    orden: Int?,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) Durazno else Color.White
        ),
        border = BorderStroke(
            width = if (seleccionado) 2.dp else 1.dp,
            color = if (seleccionado) Terracota else BordeSuave
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = seleccionado,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(checkedColor = Terracota)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    recinto.nombreOficial,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoOscuro
                )
                Text(
                    recinto.direccion,
                    fontSize = 12.sp,
                    color = TextoOscuro.copy(alpha = 0.7f)
                )
            }
            if (orden != null) {
                Text(
                    text = "$orden",
                    fontWeight = FontWeight.Bold,
                    color = Terracota,
                    fontSize = 18.sp
                )
            }
        }
    }
}