package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.model.Recinto
import kotlinx.coroutines.launch

class PlanificarVisitaViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var recintos by mutableStateOf<List<Recinto>>(emptyList())
        private set
    var recintoId by mutableStateOf<Int?>(null)
        private set
    var fechaMillis by mutableStateOf<Long?>(null)
        private set
    var hora by mutableStateOf<String?>(null)
        private set
    var participantes by mutableStateOf("")
        private set
    var requiereGuia by mutableStateOf(false)
        private set

    // El botón Guardar solo se activa cuando todo está completo
    val puedeGuardar: Boolean
        get() = recintoId != null && fechaMillis != null && hora != null &&
                (participantes.toIntOrNull() ?: 0) > 0

    init {
        viewModelScope.launch { repo.recintos.collect { recintos = it } }
    }

    fun elegirRecinto(id: Int) { recintoId = id }
    fun elegirFecha(millis: Long) { fechaMillis = millis }
    fun elegirHora(texto: String) { hora = texto }
    fun cambiarParticipantes(texto: String) { participantes = texto.filter { it.isDigit() }.take(3) }
    fun cambiarGuia(valor: Boolean) { requiereGuia = valor }

    fun guardar(onListo: () -> Unit) {
        if (!puedeGuardar) return
        viewModelScope.launch {
            repo.programarVisita(
                recintoId!!, fechaMillis!!, hora!!, participantes.toInt(), requiereGuia
            )
            onListo()
        }
    }
}