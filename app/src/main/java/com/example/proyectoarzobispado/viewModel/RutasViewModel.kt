package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.model.Ruta
import kotlinx.coroutines.launch

class RutasViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var rutas by mutableStateOf<List<Ruta>>(emptyList())
        private set

    /** Si no es null, estamos editando esa ruta en CrearRutaScreen. */
    var rutaEnEdicion by mutableStateOf<Ruta?>(null)
        private set

    init {
        // Cada vez que cambia la tabla, la lista se actualiza sola
        viewModelScope.launch {
            repo.rutas.collect { rutas = it }
        }
    }

    fun agregarRuta(ruta: Ruta) = guardar(ruta)
    fun actualizarRuta(ruta: Ruta) = guardar(ruta)

    private fun guardar(ruta: Ruta) {
        rutaEnEdicion = null
        viewModelScope.launch { repo.guardarRuta(ruta) }
    }

    fun eliminarRuta(id: Long) {
        if (rutaEnEdicion?.id == id) rutaEnEdicion = null
        viewModelScope.launch { repo.eliminarRuta(id) }
    }

    fun empezarEdicion(ruta: Ruta) { rutaEnEdicion = ruta }
    fun cancelarEdicion() { rutaEnEdicion = null }
}