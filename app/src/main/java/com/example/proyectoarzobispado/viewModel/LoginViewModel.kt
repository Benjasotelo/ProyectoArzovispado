package com.example.proyectoarzobispado.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectoarzobispado.model.Usuario

class LoginViewModel : ViewModel() {

    var textoVisible by mutableStateOf(false)
    val usuarioCorrecto = Usuario("juanperez@duocuc.cl", "123456")
    var correo by mutableStateOf("")
    var clave by mutableStateOf("")

    fun login(): Boolean{
        var validador = false

        if(correo == usuarioCorrecto.mail && clave == usuarioCorrecto.pass){
            validador = true
        }
        return validador}



}
