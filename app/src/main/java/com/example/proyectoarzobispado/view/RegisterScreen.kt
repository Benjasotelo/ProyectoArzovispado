package com.example.proyectoarzobispado.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectoarzobispado.viewmodel.RegistroViewModel

// Definición de colores basados en el diseño
val TerracottaPrimary = Color(0xFF8C4A38)
val TerracottaLight = Color(0xFFFCEAE3)
val BackgroundBeige = Color(0xFFF9F6F0)
val TextDark = Color(0xFF1F1A1C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    onRegistroExitoso: () -> Unit = {},
    vm: RegistroViewModel = viewModel()
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val transformacionClave =
        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextDark
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color = TerracottaPrimary, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountBalance,
                    contentDescription = "Logo",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crear Cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Regístrate como Turista",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 24.dp)
            )

            CampoRegistro(
                valor = vm.nombre,
                onCambio = { vm.nombre = it },
                etiqueta = "Nombre completo",
                placeholder = "Ej. Juan Pérez",
                icono = { Icon(Icons.Default.Person, null, tint = Color.Gray) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            CampoRegistro(
                valor = vm.correo,
                onCambio = { vm.correo = it },
                etiqueta = "Correo electrónico",
                placeholder = "nombre@correo.cl",
                icono = { Icon(Icons.Default.Email, null, tint = Color.Gray) },
                teclado = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(16.dp))

            CampoRegistro(
                valor = vm.clave,
                onCambio = { vm.clave = it },
                etiqueta = "Contraseña",
                placeholder = "••••••••",
                transformacion = transformacionClave,
                teclado = KeyboardType.Password,
                icono = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = "Mostrar u ocultar contraseña",
                            tint = Color.Gray
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            CampoRegistro(
                valor = vm.confirmar,
                onCambio = { vm.confirmar = it },
                etiqueta = "Confirmar contraseña",
                placeholder = "••••••••",
                transformacion = transformacionClave,
                teclado = KeyboardType.Password,
                icono = { Icon(Icons.Default.Lock, null, tint = Color.Gray) }
            )

            vm.error?.let { mensaje ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = mensaje,
                    color = Color(0xFF8A2A22),
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { vm.registrar(onExito = onRegistroExitoso) },
                enabled = !vm.cargando,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "¿Ya tienes una cuenta?", color = Color.Black, fontSize = 14.sp)
            TextButton(onClick = { navController.popBackStack() }) {
                Text(text = "Inicia sesión", color = TerracottaPrimary, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CampoRegistro(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    placeholder: String,
    icono: @Composable () -> Unit,
    transformacion: VisualTransformation = VisualTransformation.None,
    teclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        placeholder = { Text(placeholder) },
        trailingIcon = icono,
        singleLine = true,
        visualTransformation = transformacion,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TerracottaPrimary,
            focusedLabelColor = TerracottaPrimary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}