package com.example.proyectoarzobispado.navigation

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login/{rol}"
    const val HOME = "home"

    fun login(rol: String) = "login/$rol"
}
