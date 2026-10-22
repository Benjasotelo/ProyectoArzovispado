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

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    private var recintos by mutableStateOf<List<Recinto>>(emptyList())

    var filtroEstado by mutableStateOf<EstadoRecinto?>(null)
        private set
    var recintoSeleccionado by mutableStateOf<Recinto?>(null)
        private set
    var iniciales by mutableStateOf("")
        private set

    val recintosFiltrados: List<Recinto>
        get() = filtroEstado?.let { f -> recintos.filter { it.estado == f } } ?: recintos

    init {
        viewModelScope.launch {
            repo.recintos.collect { lista ->
                recintos = lista
                // mantiene actualizada la tarjeta si el recinto cambió
                recintoSeleccionado = recintoSeleccionado?.let { sel -> lista.find { it.id == sel.id } }
            }
        }
        viewModelScope.launch { iniciales = repo.usuarioActual()?.iniciales ?: "" }
    }

    fun seleccionarFiltro(estado: EstadoRecinto?) {
        filtroEstado = if (filtroEstado == estado) null else estado
        if (recintoSeleccionado !in recintosFiltrados) recintoSeleccionado = null
    }

    fun seleccionarRecinto(recinto: Recinto) { recintoSeleccionado = recinto }

    fun cerrarFicha() { recintoSeleccionado = null }
}