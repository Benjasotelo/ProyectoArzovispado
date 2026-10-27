package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.model.EstadoRecinto
import com.example.proyectoarzobispado.model.Evento
import com.example.proyectoarzobispado.model.Recinto
import kotlinx.coroutines.launch

class MiRecintoViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var recintos by mutableStateOf<List<Recinto>>(emptyList())
        private set

    var eventos by mutableStateOf<List<Evento>>(emptyList())
        private set

    // Guardamos solo el id; el Recinto completo se busca en la lista actual
    private var idSeleccionado by mutableStateOf<Int?>(null)

    val recintoSeleccionado: Recinto?
        get() = recintos.find { it.id == idSeleccionado }

    // Filtramos los eventos del recinto elegido
    val eventosSeleccionado: List<Evento>
        get() = eventos.filter { it.recintoId == idSeleccionado }

    init {
        viewModelScope.launch {
            repo.recintos.collect { lista ->
                recintos = lista
                // la primera vez, deja elegido el primer recinto
                if (idSeleccionado == null) idSeleccionado = lista.firstOrNull()?.id
            }
        }
        viewModelScope.launch {
            repo.eventos.collect { eventos = it }
        }
    }

    fun seleccionarRecinto(id: Int) { idSeleccionado = id }

    fun cambiarEstado(nuevoEstado: EstadoRecinto) {
        val id = idSeleccionado ?: return
        viewModelScope.launch { repo.actualizarEstado(id, nuevoEstado) }
    }

    fun agregarEvento(titulo: String, fecha: String, hora: String, descripcion: String) {
        val id = idSeleccionado ?: return
        if (titulo.isBlank()) return
        viewModelScope.launch {
            repo.agregarEvento(id, titulo, descripcion, fecha, hora)
        }
    }

    fun eliminarEvento(id: Long) {
        viewModelScope.launch { repo.eliminarEvento(id) }
    }

    var modoEdicion by mutableStateOf(false)
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set


    // Campos del formulario mientras se edita
    var nombreEdit by mutableStateOf("")
        private set
    var direccionEdit by mutableStateOf("")
        private set
    var tipoEdit by mutableStateOf("")
        private set
    var descripcionEdit by mutableStateOf("")
        private set

    var horarioEdit by mutableStateOf("")
        private set

    fun cancelarEdicion() {
        modoEdicion = false
        mensaje = null
    }

    fun onNombreChange(valor: String) { nombreEdit = valor }
    fun onDireccionChange(valor: String) { direccionEdit = valor }
    fun onTipoChange(valor: String) { tipoEdit = valor }
    fun onDescripcionChange(valor: String) { descripcionEdit = valor }

    fun onHorarioChange(valor: String) { horarioEdit = valor }

    fun limpiarMensaje() { mensaje = null }
    fun abrirEdicion() {
        val r = recintoSeleccionado ?: return
        nombreEdit = r.nombreOficial
        direccionEdit = r.direccion
        tipoEdit = r.tipoRecinto
        descripcionEdit = r.descripcionHistorica
        horarioEdit = r.horario
        modoEdicion = true
        mensaje = null
    }

    fun guardarFicha() {
        val r = recintoSeleccionado ?: return
        if (nombreEdit.isBlank() || direccionEdit.isBlank()) {
            mensaje = "El nombre oficial y la dirección son obligatorios"
            return
        }
        viewModelScope.launch {
            try {
                repo.actualizarFicha(
                    r.copy(
                        nombreOficial = nombreEdit.trim(),
                        direccion = direccionEdit.trim(),
                        tipoRecinto = tipoEdit.trim(),
                        descripcionHistorica = descripcionEdit.trim(),
                        horario = horarioEdit.trim()
                    )
                )
                modoEdicion = false
                mensaje = "Ficha actualizada correctamente"
            } catch (e: Exception) {
                mensaje = "Error al guardar: ${e.message}"
            }
        }
    }}
