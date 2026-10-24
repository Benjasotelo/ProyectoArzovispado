package com.example.proyectoarzobispado.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"

    // Pestañas del Home
    const val TAB_MAPA = "tab_mapa"
    const val TAB_RUTAS = "tab_rutas"
    const val TAB_AVISOS = "tab_avisos"
    const val TAB_MI_RECINTO = "tab_mi_recinto"
    const val TAB_PERFIL = "tab_perfil"
    const val ESCANER = "escaner"
    // Ficha patrimonial: {recintoId} es un parámetro que se rellena al navegar
    const val FICHA = "ficha/{recintoId}"
    fun ficha(recintoId: Int) = "ficha/$recintoId"

    // Crear ruta (dentro del tab de Rutas)
    const val CREAR_RUTA = "crear_ruta"
}