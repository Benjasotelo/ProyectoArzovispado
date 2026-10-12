package com.example.proyectoarzobispado.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectoarzobispado.view.HomeScreen
import com.example.proyectoarzobispado.view.LoginScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = Routes.LOGIN
    ){
        composable(Routes.LOGIN) { LoginScreen() }
        composable(Routes.HOME) { HomeScreen() }
    }
}