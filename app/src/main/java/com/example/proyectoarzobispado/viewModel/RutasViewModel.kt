package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.proyectoarzobispado.model.Ruta

class RutasViewModel(app: Application) : AndroidViewModel(app) {

    var rutas by mutableStateOf<List<Ruta>>(emptyList())
        private set

    /** Si no es null, estamos editando esa ruta en CrearRutaScreen. */
    var rutaEnEdicion by mutableStateOf<Ruta?>(null)
        private set

    fun agregarRuta(ruta: Ruta) {
        rutas = rutas + ruta
        rutaEnEdicion = null
    }

    fun actualizarRuta(ruta: Ruta) {
        rutas = rutas.map { if (it.id == ruta.id) ruta else it }
        rutaEnEdicion = null
    }

    fun eliminarRuta(id: Long) {
        rutas = rutas.filter { it.id != id }
        if (rutaEnEdicion?.id == id) rutaEnEdicion = null
    }

    fun empezarEdicion(ruta: Ruta) {
        rutaEnEdicion = ruta
    }

    fun cancelarEdicion() {
        rutaEnEdicion = null
    }
}