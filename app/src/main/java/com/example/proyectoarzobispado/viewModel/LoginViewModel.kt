package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import kotlinx.coroutines.launch

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var correo by mutableStateOf("")
    var clave by mutableStateOf("")
    var textoVisible by mutableStateOf(false)   // muestra "Credenciales invalidas"
    var cargando by mutableStateOf(false)

    fun login(onExito: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            val ok = repo.iniciarSesion(correo, clave)
            cargando = false
            if (ok) onExito() else textoVisible = true
        }
    }
}