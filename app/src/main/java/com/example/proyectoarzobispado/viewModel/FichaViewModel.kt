package com.example.proyectoarzobispado.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.model.Recinto
import kotlinx.coroutines.launch

class FichaViewModel(
    app: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    // Argumento "recintoId" que llega desde la navegación
    private val recintoId: Int = checkNotNull(savedStateHandle.get<Int>("recintoId"))

    var recinto by mutableStateOf<Recinto?>(null)
        private set

    init {
        viewModelScope.launch {
            repo.recinto(recintoId).collect { recinto = it }
        }
    }
}