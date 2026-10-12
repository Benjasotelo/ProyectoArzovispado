package com.example.proyectoarzobispado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.proyectoarzobispado.navigation.AppNavigation
import com.example.proyectoarzobispado.ui.theme.ProyectoArzobispadoTheme
import com.example.proyectoarzobispado.view.LoginScreen
import com.example.proyectoarzobispado.view.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoArzobispadoTheme {
                AppNavigation()
            }
        }
    }
}