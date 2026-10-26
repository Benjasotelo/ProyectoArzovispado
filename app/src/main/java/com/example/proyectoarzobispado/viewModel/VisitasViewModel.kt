package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.model.Visita
import kotlinx.coroutines.launch

class VisitasViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var visitas by mutableStateOf<List<Visita>>(emptyList())
        private set
    private var recintos by mutableStateOf<List<Recinto>>(emptyList())

    init {
        viewModelScope.launch { repo.visitas().collect { visitas = it } }
        viewModelScope.launch { repo.recintos.collect { recintos = it } }
    }

    fun nombreRecinto(id: Int): String =
        recintos.find { it.id == id }?.nombreOficial ?: "Recinto"

    fun confirmar(id: Long) { viewModelScope.launch { repo.confirmarVisita(id) } }
    fun eliminar(id: Long) { viewModelScope.launch { repo.eliminarVisita(id) } }
}