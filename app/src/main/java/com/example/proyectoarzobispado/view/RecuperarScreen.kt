package com.example.proyectoarzobispado.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.ui.theme.Crema
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.RecuperarViewModel

@Composable
fun RecuperarScreen(onVolver: () -> Unit, vm: RecuperarViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Crema)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = TextoOscuro)
        }

        Text("Recuperar contraseña", style = MaterialTheme.typography.headlineMedium, color = TextoOscuro)

        if (vm.exito) {
            Text(
                "Tu contraseña fue actualizada. Ya puedes iniciar sesión con la nueva.",
                fontSize = 15.sp,
                color = TextoOscuro
            )
            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Terracota, contentColor = Color.White)
            ) { Text("Volver a iniciar sesión") }
        } else {
            Text(
                "Ingresa el correo de tu cuenta y elige una contraseña nueva.",
                fontSize = 14.sp,
                color = TextoOscuro.copy(alpha = 0.7f)
            )

            CampoRecuperar(vm.correo, { vm.correo = it }, "Correo", KeyboardType.Email, false)
            CampoRecuperar(vm.clave, { vm.clave = it }, "Contraseña nueva", KeyboardType.Password, true)
            CampoRecuperar(vm.confirmar, { vm.confirmar = it }, "Repetir contraseña", KeyboardType.Password, true)

            vm.error?.let {
                Text(it, color = Color(0xFFB3261E), fontSize = 13.sp)
            }

            Button(
                onClick = { vm.cambiar() },
                enabled = !vm.cargando,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Terracota, contentColor = Color.White)
            ) { Text(if (vm.cargando) "Guardando..." else "Cambiar contraseña") }
        }
    }
}

@Composable
private fun CampoRecuperar(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    teclado: KeyboardType,
    oculto: Boolean
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        singleLine = true,
        visualTransformation = if (oculto) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Terracota,
            focusedLabelColor = Terracota
        ),
        modifier = Modifier.fillMaxWidth()
    )
}