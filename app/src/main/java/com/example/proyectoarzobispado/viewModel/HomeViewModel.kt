package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import com.example.proyectoarzobispado.model.Ruta
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.model.EstadoRecinto
import com.example.proyectoarzobispado.model.Recinto
import kotlinx.coroutines.launch
import java.text.Normalizer

// Pasa a minúsculas y quita tildes: "Basílica" -> "basilica"
private fun normalizar(texto: String): String =
    Normalizer.normalize(texto.lowercase(), Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = Repositorio.obtener(app)

    private var recintos by mutableStateOf<List<Recinto>>(emptyList())

    var filtroEstado by mutableStateOf<EstadoRecinto?>(null)
        private set
    var recintoSeleccionado by mutableStateOf<Recinto?>(null)
        private set
    var iniciales by mutableStateOf("")
        private set
    var consulta by mutableStateOf("")
        private set

    /** Ruta que se está mostrando en el mapa (null = ninguna). */
    var rutaEnMapa by mutableStateOf<Ruta?>(null)
        private set

    /** Recintos de la ruta, en orden de las paradas. */
    val puntosRuta: List<Recinto>
        get() = rutaEnMapa?.paradas
            ?.sortedBy { it.orden }
            ?.mapNotNull { parada -> recintos.find { it.id == parada.recintoId } }
            ?: emptyList()

    val recintosFiltrados: List<Recinto>
        get() = filtroEstado?.let { f -> recintos.filter { it.estado == f } } ?: recintos

    // Recintos que coinciden con lo escrito. Todas las palabras deben aparecer.
    val resultados: List<Recinto>
        get() {
            val palabras = normalizar(consulta).split(" ").filter { it.isNotBlank() }
            if (palabras.isEmpty()) return emptyList()
            return recintos
                .filter { r ->
                    val texto = normalizar("${r.nombreOficial} ${r.direccion} ${r.tipoRecinto}")
                    palabras.all { texto.contains(it) }
                }
                // primero los que coinciden por nombre
                .sortedBy { r ->
                    val nombre = normalizar(r.nombreOficial)
                    if (palabras.all { nombre.contains(it) }) 0 else 1
                }
        }

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

    fun actualizarConsulta(texto: String) { consulta = texto }

    // Al elegir un resultado: limpia la búsqueda y deja el recinto seleccionado en el mapa.
    // Si un filtro de estado lo ocultaría, lo quita.
    fun elegirResultado(recinto: Recinto) {
        consulta = ""
        if (filtroEstado != null && recinto.estado != filtroEstado) filtroEstado = null
        recintoSeleccionado = recinto
    }

    fun mostrarRuta(ruta: Ruta) {
        rutaEnMapa = ruta
        filtroEstado = null
        recintoSeleccionado = null
        consulta = ""
    }

    fun limpiarRuta() {
        rutaEnMapa = null
    }
}