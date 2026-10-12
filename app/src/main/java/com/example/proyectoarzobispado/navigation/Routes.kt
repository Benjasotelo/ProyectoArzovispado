package com.example.proyectoarzobispado.navigation

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val WELCOME = "welcome"

    fun login(rol: String) = "login/$rol"
}