package com.example.proyectoarzobispado.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectoarzobispado.view.HomeScreen
import com.example.proyectoarzobispado.view.LoginScreen
import com.example.proyectoarzobispado.view.RegistroScreen
import com.example.proyectoarzobispado.view.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onRolSeleccionado = { rol ->
                    navController.navigate(Routes.login(rol))
                }
            )
        }

        composable(
            route = Routes.LOGIN,
            arguments = listOf(navArgument("rol") { type = NavType.StringType })
        ) { backStackEntry ->
            val rol = backStackEntry.arguments?.getString("rol") ?: ""
            LoginScreen(
                navController = navController,
                rol = rol,
                onBack = { navController.popBackStack() },
                onInvitado = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.REGISTER) {
            RegistroScreen(navController = navController)
        }

        composable(Routes.HOME) {
            HomeScreen(
                onCerrarSesion = {
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}