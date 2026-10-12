package com.example.proyectoarzobispado.navigation

import android.net.Uri

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login/{rol}"
    const val HOME = "home"

    const val REGISTER = "register"

    fun login(rol: String) = "login/${Uri.encode(rol)}"
}
