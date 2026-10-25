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

class FavoritosViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repositorio.obtener(app)

    var favoritos by mutableStateOf<List<Recinto>>(emptyList())
        private set

    init {
        viewModelScope.launch { repo.favoritos().collect { favoritos = it } }
    }

    fun quitar(recintoId: Int) {
        viewModelScope.launch { repo.alternarFavorito(recintoId, esFavoritoAhora = true) }
    }
}