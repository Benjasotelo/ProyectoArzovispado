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

class RecuperarViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var correo by mutableStateOf("")
    var clave by mutableStateOf("")
    var confirmar by mutableStateOf("")
    var error by mutableStateOf<String?>(null)
        private set
    var exito by mutableStateOf(false)
        private set
    var cargando by mutableStateOf(false)
        private set

    fun cambiar() {
        error = when {
            !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> "Ingresa un correo válido"
            clave.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            clave != confirmar -> "Las contraseñas no coinciden"
            else -> null
        }
        if (error != null) return

        viewModelScope.launch {
            cargando = true
            val ok = repo.cambiarClave(correo, clave)
            cargando = false
            if (ok) exito = true else error = "No existe una cuenta con ese correo"
        }
    }
}