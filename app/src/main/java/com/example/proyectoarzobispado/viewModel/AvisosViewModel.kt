package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.model.Aviso
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.model.Rol
import kotlinx.coroutines.launch

class AvisosViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var avisos by mutableStateOf<List<Aviso>>(emptyList())
        private set
    var recintos by mutableStateOf<List<Recinto>>(emptyList())
        private set
    // Solo el Encargado puede publicar y borrar avisos
    var esEncargado by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch { repo.avisos.collect { avisos = it } }
        viewModelScope.launch { repo.recintos.collect { recintos = it } }
        viewModelScope.launch { esEncargado = repo.usuarioActual()?.rol == Rol.ENCARGADO }
    }

    fun nombreRecinto(id: Int): String =
        recintos.find { it.id == id }?.nombreOficial ?: "Recinto"

    fun publicar(recintoId: Int, titulo: String, mensaje: String) {
        viewModelScope.launch { repo.publicarAviso(recintoId, titulo, mensaje) }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch { repo.eliminarAviso(id) }
    }
}