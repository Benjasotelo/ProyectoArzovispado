package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import kotlinx.coroutines.launch

class RegistroViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var clave by mutableStateOf("")
    var confirmar by mutableStateOf("")
    var error by mutableStateOf<String?>(null)
    var cargando by mutableStateOf(false)

    private fun validar(): String? = when {
        nombre.isBlank() -> "Ingresa tu nombre"
        !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> "El correo no es válido"
        clave.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
        clave != confirmar -> "Las contraseñas no coinciden"
        else -> null
    }

    fun registrar(onExito: () -> Unit) {
        error = validar()
        if (error != null) return
        viewModelScope.launch {
            cargando = true
            error = repo.registrar(nombre, correo, clave)
            cargando = false
            if (error == null) onExito()
        }
    }
}