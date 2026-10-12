package com.example.proyectoarzobispado.navigation

import android.net.Uri

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login/{rol}"
    const val HOME = "home"

    // Pestañas del Home
    const val TAB_MAPA = "tab_mapa"
    const val TAB_RUTAS = "tab_rutas"
    const val TAB_AVISOS = "tab_avisos"
    const val TAB_PERFIL = "tab_perfil"

    fun login(rol: String) = "login/${Uri.encode(rol)}"
}