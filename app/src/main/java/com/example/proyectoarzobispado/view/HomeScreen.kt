package com.example.proyectoarzobispado.view

import com.example.proyectoarzobispado.viewmodel.RutasViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyectoarzobispado.model.Rol
import com.example.proyectoarzobispado.navigation.Routes
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Crema
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.HomeViewModel
import com.example.proyectoarzobispado.viewmodel.PerfilViewModel

private data class TabItem(val route: String, val label: String, val icon: ImageVector)

// Las pestañas dependen del rol. null = invitado (sin sesión) o usuario aún cargando.
private fun pestanasPara(rol: Rol?): List<TabItem> = buildList {
    add(TabItem(Routes.TAB_MAPA, "Mapa", Icons.Filled.Map))
    add(TabItem(Routes.TAB_RUTAS, "Rutas", Icons.Filled.Route))
    add(TabItem(Routes.TAB_AVISOS, "Avisos", Icons.Filled.Notifications))
    if (rol == Rol.ENCARGADO) {
        add(TabItem(Routes.TAB_MI_RECINTO, "Mi recinto", Icons.Filled.AccountBalance))
    }
    add(TabItem(Routes.TAB_PERFIL, "Perfil", Icons.Filled.Person))
}

@Composable
fun HomeScreen(onCerrarSesion: () -> Unit, sesionVm: PerfilViewModel = viewModel()) {
    val homeVm: HomeViewModel = viewModel()
    val tabNav = rememberNavController()
    val backStack by tabNav.currentBackStackEntryAsState()
    val rutaActual = backStack?.destination?.route

    val tabs = pestanasPara(sesionVm.usuario?.rol)

    val irA: (String) -> Unit = { ruta ->
        tabNav.navigate(ruta) {
            popUpTo(tabNav.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Box(Modifier.fillMaxSize().background(Crema)) {
        NavHost(
            navController = tabNav,
            startDestination = Routes.TAB_MAPA,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Routes.TAB_MAPA) {
                MapaScreen(
                    onPerfil = { irA(Routes.TAB_PERFIL) },
                    onAbrirFicha = { id -> tabNav.navigate(Routes.ficha(id)) },
                    onEscanear = { tabNav.navigate(Routes.ESCANER) },
                    vm = homeVm

                )
            }
            composable(
                route = Routes.FICHA,
                arguments = listOf(navArgument("recintoId") { type = NavType.IntType })
            ) {
                FichaScreen(onVolver = { tabNav.popBackStack() })
            }
            composable(Routes.TAB_RUTAS) { backStackEntry ->
                val rutasVm: RutasViewModel = viewModel(backStackEntry)
                RutasScreen(
                    vm = rutasVm,
                    onCrearRuta = {
                        rutasVm.cancelarEdicion()
                        tabNav.navigate(Routes.CREAR_RUTA)
                    },
                    onVerEnMapa = { ruta ->
                        homeVm.mostrarRuta(ruta)
                        irA(Routes.TAB_MAPA)
                    },
                    onEditarRuta = { ruta ->
                        rutasVm.empezarEdicion(ruta)
                        tabNav.navigate(Routes.CREAR_RUTA)
                    },
                    onRutaEliminada = { id ->
                        if (homeVm.rutaEnMapa?.id == id) {
                            homeVm.limpiarRuta()
                        }
                    }

                )
            }
            composable(Routes.CREAR_RUTA) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    tabNav.getBackStackEntry(Routes.TAB_RUTAS)
                }
                val rutasVm: RutasViewModel = viewModel(parentEntry)
                CrearRutaScreen(
                    rutaInicial = rutasVm.rutaEnEdicion,
                    onVolver = {
                        rutasVm.cancelarEdicion()
                        tabNav.popBackStack()
                    },
                    onGuardar = { ruta ->
                        if (rutasVm.rutaEnEdicion != null) {
                            rutasVm.actualizarRuta(ruta)
                        } else {
                            rutasVm.agregarRuta(ruta)
                        }
                        tabNav.popBackStack()
                    }
                )
            }
            composable(Routes.TAB_AVISOS) { AvisosScreen() }
            composable(Routes.TAB_MI_RECINTO) { MiRecintoScreen() }
            composable(Routes.FAVORITOS) {
                FavoritosScreen(
                    onVolver = { tabNav.popBackStack() },
                    onAbrirFicha = { id -> tabNav.navigate(Routes.ficha(id)) }
                )
            }
            composable(Routes.TAB_PERFIL) {
                PerfilScreen(onCerrarSesion, onFavoritos = { tabNav.navigate(Routes.FAVORITOS) })
            }
            composable(Routes.ESCANER) {
                EscanerScreen(
                    onVolver = { tabNav.popBackStack() },
                    onRecintoEncontrado = { id ->
                        // Abre la ficha y saca el escáner del historial: "atrás" vuelve al mapa
                        tabNav.navigate(Routes.ficha(id)) {
                            popUpTo(Routes.ESCANER) { inclusive = true }
                        }
                    }
                )
            }
        }

        if (rutaActual != Routes.CREAR_RUTA) {
            BarraFlotante(
                tabs = tabs,
                rutaActual = when (rutaActual) {
                    Routes.FICHA, Routes.ESCANER -> Routes.TAB_MAPA
                    Routes.FAVORITOS -> Routes.TAB_PERFIL
                    else -> rutaActual
                },
                onTabClick = irA,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun BarraFlotante(
    tabs: List<TabItem>,
    rutaActual: String?,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color.White.copy(alpha = 0.94f),
        shadowElevation = 10.dp,
        border = BorderStroke(0.5.dp, BordeSuave)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tabs.forEach { tab ->
                val seleccionado = rutaActual == tab.route
                val colorContenido = if (seleccionado) Terracota else TextoOscuro.copy(alpha = 0.6f)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabClick(tab.route) }
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(32.dp)
                            .clip(CircleShape)
                            .background(if (seleccionado) Durazno else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = colorContenido,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = tab.label,
                        fontSize = 12.sp,
                        maxLines = 1,
                        color = colorContenido
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceholderTab(titulo: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(titulo, style = MaterialTheme.typography.titleMedium, color = TextoOscuro)
    }
}