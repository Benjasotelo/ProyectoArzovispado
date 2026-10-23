package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Formato del QR de cada templo: redpatrimonial://recinto/3
private const val PREFIJO_QR = "redpatrimonial://recinto/"

// Devuelve el id del recinto, o null si el texto no tiene el formato esperado
private fun idDesdeQr(texto: String): Int? =
    if (texto.startsWith(PREFIJO_QR)) texto.removePrefix(PREFIJO_QR).trim().toIntOrNull() else null

class EscanerViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)
    private var procesando = false

    var mensaje by mutableStateOf<String?>(null)
        private set

    fun procesar(texto: String, onEncontrado: (Int) -> Unit) {
        if (procesando) return
        procesando = true
        viewModelScope.launch {
            val id = idDesdeQr(texto)
            // first() toma el valor actual del Flow de Room (null si el id no existe)
            val recinto = if (id != null) repo.recinto(id).first() else null
            if (recinto != null) {
                onEncontrado(recinto.id)
            } else {
                mensaje = "Este código QR no pertenece a la Red Patrimonial"
                delay(2500)
                mensaje = null
                procesando = false
            }
        }
    }
}