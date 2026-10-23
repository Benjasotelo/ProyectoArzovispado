package com.example.proyectoarzobispado.view

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.viewmodel.LoginViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectoarzobispado.navigation.Routes
import com.example.proyectoarzobispado.ui.theme.*
import androidx.compose.material.icons.filled.ErrorOutline

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginExitoso: () -> Unit = {},
    onInvitado: () -> Unit = {}
) {

    val viewModel: LoginViewModel = viewModel()
    var verClave by remember { mutableStateOf(false) }

    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Terracota,
        unfocusedBorderColor = Terracota.copy(alpha = 0.6f),
        focusedLabelColor = Terracota,
        unfocusedLabelColor = Terracota.copy(alpha = 0.8f),
        cursorColor = Terracota,
        focusedTextColor = TextoOscuro,
        unfocusedTextColor = TextoOscuro,
        errorBorderColor = ErrorTexto,
        errorLabelColor = ErrorTexto,
        errorTextColor = TextoOscuro,
        errorCursorColor = ErrorTexto
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Crema)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.height(48.dp))
        LogoApp(56.dp)
        Spacer(Modifier.height(24.dp))
        Text("Bienvenido", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
        Spacer(Modifier.height(4.dp))
        Text("Ingresa a tu cuenta", fontSize = 16.sp, color = TextoOscuro.copy(alpha = 0.8f))
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.correo,
            onValueChange = { viewModel.correo = it; viewModel.textoVisible = false },
            isError = viewModel.textoVisible,
            label = { Text("Correo electrónico") },
            placeholder = { Text("nombre@correo.cl") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = coloresCampo,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            trailingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = TextoOscuro) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.clave,
            onValueChange = { viewModel.clave = it; viewModel.textoVisible = false },
            isError = viewModel.textoVisible,
            label = { Text("Contraseña") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = coloresCampo,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (verClave) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { verClave = !verClave }) {
                    Icon(
                        if (verClave) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = "Mostrar u ocultar contraseña",
                        tint = TextoOscuro
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(onClick = { }, modifier = Modifier.align(Alignment.End)) {
            Text("¿Olvidaste tu contraseña?", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Terracota)
        }
        Spacer(Modifier.height(8.dp))

        if (viewModel.textoVisible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ErrorFondo, RoundedCornerShape(size = 12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Filled.ErrorOutline,
                    contentDescription = null,
                    tint = ErrorTexto,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    "Correo o contraseña incorrectos",
                    fontSize = 14.sp,
                    color = ErrorTexto
                )
            }
            Spacer(Modifier.height(12.dp))
        }

        Button(
            onClick = {
                viewModel.textoVisible = false
                viewModel.login(onExito = onLoginExitoso)
            },
            enabled = !viewModel.cargando,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Terracota, contentColor = SobreTerracota),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Ingresar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Terracota.copy(alpha = 0.4f))
            Text("  o  ", fontSize = 12.sp, color = TextoOscuro.copy(alpha = 0.7f))
            HorizontalDivider(modifier = Modifier.weight(1f), color = Terracota.copy(alpha = 0.4f))
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onInvitado,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Durazno, contentColor = TextoOscuro),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Continuar como invitado", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(Modifier.height(16.dp))

        Text(text = "¿No tienes una cuenta? ", fontSize = 16.sp, color = TextoOscuro.copy(alpha = 0.6f), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        TextButton(onClick = { navController.navigate(Routes.REGISTER) }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Regístrate", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Terracota)
        }
            Spacer(Modifier.height(48.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Lock, contentDescription = null, tint = Terracota, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text("Tus datos están protegidos", fontSize = 12.sp, color = TextoOscuro.copy(alpha = 0.6f), textAlign = TextAlign.Center)
        }
    }
}