package com.example.proyectoarzobispado.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectoarzobispado.model.EstadoRecinto
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.model.RecintosEjemplo

class HomeViewModel : ViewModel() {
    private val recintos = RecintosEjemplo.lista

    var filtroEstado by mutableStateOf<EstadoRecinto?>(null)
        private set
    var recintoSeleccionado by mutableStateOf<Recinto?>(null)
        private set

    val recintosFiltrados: List<Recinto>
        get() = filtroEstado?.let { f -> recintos.filter { it.estado == f } } ?: recintos

    fun seleccionarFiltro(estado: EstadoRecinto?) {
        filtroEstado = if (filtroEstado == estado) null else estado
        if (recintoSeleccionado !in recintosFiltrados) recintoSeleccionado = null
    }

    fun seleccionarRecinto(recinto: Recinto) { recintoSeleccionado = recinto }

    fun cerrarFicha() { recintoSeleccionado = null }
}