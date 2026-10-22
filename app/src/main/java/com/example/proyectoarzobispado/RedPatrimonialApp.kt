package com.example.proyectoarzobispado

import android.app.Application
import com.example.proyectoarzobispado.data.Repositorio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RedPatrimonialApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            Repositorio.obtener(this@RedPatrimonialApp).sembrarDatosIniciales()
        }
    }
}