package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.proyectoarzobispado.model.Ruta

class RutasViewModel(app: Application) : AndroidViewModel(app) {

    /** Lista de rutas del usuario (por ahora solo en memoria). */
    var rutas by mutableStateOf<List<Ruta>>(emptyList())
        private set

    /**
     * Más adelante aquí agregaremos la ruta creada.
     * Por ahora solo deja el hueco listo.
     */
    fun agregarRuta(ruta: Ruta) {
        rutas = rutas + ruta
    }

    fun eliminarRuta(id: Long) {
        rutas = rutas.filter { it.id != id }
    }
}