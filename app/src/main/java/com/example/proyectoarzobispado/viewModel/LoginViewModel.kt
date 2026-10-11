package com.example.proyectoarzobispado.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var correo by mutableStateOf("")
    var clave by mutableStateOf("")
}