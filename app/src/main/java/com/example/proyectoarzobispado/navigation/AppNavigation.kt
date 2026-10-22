package com.example.proyectoarzobispado.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectoarzobispado.data.Repositorio
import com.example.proyectoarzobispado.view.HomeScreen
import com.example.proyectoarzobispado.view.LoginScreen
import com.example.proyectoarzobispado.view.RegistroScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val repo = Repositorio.obtener(LocalContext.current)
    val inicio = remember { if (repo.haySesion()) Routes.HOME else Routes.LOGIN }

    NavHost(navController = navController, startDestination = inicio) {

        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                onLoginExitoso = { irAlHome(navController) },
                onInvitado = {
                    repo.cerrarSesion()
                    irAlHome(navController)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegistroScreen(
                navController = navController,
                onRegistroExitoso = { irAlHome(navController) }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onCerrarSesion = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}

// Va al Home y borra login/registro del historial (el botón "atrás" no vuelve a ellos)
private fun irAlHome(navController: NavController) {
    navController.navigate(Routes.HOME) {
        popUpTo(navController.graph.id) { inclusive = true }
    }
}