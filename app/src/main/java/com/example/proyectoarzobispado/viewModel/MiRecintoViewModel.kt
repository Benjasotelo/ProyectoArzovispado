package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.model.EstadoRecinto
import com.example.proyectoarzobispado.model.Recinto
import kotlinx.coroutines.launch

class MiRecintoViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var recintos by mutableStateOf<List<Recinto>>(emptyList())
        private set

    // Guardamos solo el id; el Recinto completo se busca en la lista actual
    private var idSeleccionado by mutableStateOf<Int?>(null)

    val recintoSeleccionado: Recinto?
        get() = recintos.find { it.id == idSeleccionado }

    init {
        viewModelScope.launch {
            repo.recintos.collect { lista ->
                recintos = lista
                // la primera vez, deja elegido el primer recinto
                if (idSeleccionado == null) idSeleccionado = lista.firstOrNull()?.id
            }
        }
    }

    fun seleccionarRecinto(id: Int) { idSeleccionado = id }

    fun cambiarEstado(nuevoEstado: EstadoRecinto) {
        val id = idSeleccionado ?: return
        viewModelScope.launch { repo.actualizarEstado(id, nuevoEstado) }
    }
}