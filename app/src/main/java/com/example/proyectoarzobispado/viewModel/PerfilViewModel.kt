package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.model.Usuario
import kotlinx.coroutines.launch

class PerfilViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var usuario by mutableStateOf<Usuario?>(null)
        private set

    init {
        viewModelScope.launch { usuario = repo.usuarioActual() }
    }

    fun cerrarSesion() = repo.cerrarSesion()
}