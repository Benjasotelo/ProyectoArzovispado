package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.model.ParadaRuta
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.model.Ruta
import kotlinx.coroutines.launch

class CrearRutaViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = Repositorio.obtener(app)

    var recintos by mutableStateOf<List<Recinto>>(emptyList())
        private set

    /** IDs de los recintos elegidos, en el orden en que el usuario los tocó. */
    var idsSeleccionados by mutableStateOf<List<Int>>(emptyList())
        private set

    var nombre by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            repo.recintos.collect { lista ->
                recintos = lista
            }
        }
    }

    fun actualizarNombre(texto: String) {
        nombre = texto
    }

    /** Si ya está seleccionado, lo quita. Si no, lo agrega al final. */
    fun alternarRecinto(id: Int) {
        idsSeleccionados = if (id in idsSeleccionados) {
            idsSeleccionados - id
        } else {
            idsSeleccionados + id
        }
    }

    fun estaSeleccionado(id: Int): Boolean = id in idsSeleccionados

    /**
     * Calcula minutos caminando entre dos recintos.
     * Misma lógica del mapa: ~5 km/h ≈ 83 metros por minuto.
     */
    private fun minutosEntre(a: Recinto, b: Recinto): Int {
        val resultado = FloatArray(1)
        Location.distanceBetween(
            a.latitud, a.longitud,
            b.latitud, b.longitud,
            resultado
        )
        return (resultado[0] / 83f).toInt().coerceAtLeast(1)
    }

    /**
     * Arma la Ruta a partir de la selección actual.
     * Devuelve null si faltan datos (menos de 2 paradas o nombre vacío).
     */
    fun construirRuta(): Ruta? {
        if (idsSeleccionados.size < 2) return null
        val nombreFinal = nombre.trim().ifBlank { "Mi ruta" }

        val paradas = mutableListOf<ParadaRuta>()
        var tiempoTotal = 0

        idsSeleccionados.forEachIndexed { indice, id ->
            val minutosDesdeAnterior = if (indice == 0) {
                0
            } else {
                val anterior = recintos.first { it.id == idsSeleccionados[indice - 1] }
                val actual = recintos.first { it.id == id }
                minutosEntre(anterior, actual)
            }
            tiempoTotal += minutosDesdeAnterior
            paradas.add(
                ParadaRuta(
                    recintoId = id,
                    orden = indice + 1,
                    tiempoEstimadoMinutos = minutosDesdeAnterior
                )
            )
        }

        return Ruta(
            id = System.currentTimeMillis(), // id temporal único en memoria
            nombre = nombreFinal,
            paradas = paradas,
            tiempoTotalMinutos = tiempoTotal
        )
    }
}