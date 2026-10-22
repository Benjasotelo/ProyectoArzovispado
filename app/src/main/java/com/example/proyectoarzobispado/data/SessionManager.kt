package com.example.proyectoarzobispado.data

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("sesion", Context.MODE_PRIVATE)

    fun guardar(idUsuario: Long) = prefs.edit().putLong(CLAVE, idUsuario).apply()
    fun usuarioId(): Long = prefs.getLong(CLAVE, -1L)
    fun cerrar() = prefs.edit().remove(CLAVE).apply()

    private companion object { const val CLAVE = "usuario_id" }
}
