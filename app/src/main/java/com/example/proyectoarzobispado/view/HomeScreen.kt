package com.example.proyectoarzobispado.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyectoarzobispado.navigation.Routes
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Crema
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro

private data class TabItem(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    TabItem(Routes.TAB_MAPA, "Mapa", Icons.Filled.Map),
    TabItem(Routes.TAB_RUTAS, "Rutas", Icons.Filled.Route),
    TabItem(Routes.TAB_AVISOS, "Avisos", Icons.Filled.Notifications),
    TabItem(Routes.TAB_PERFIL, "Perfil", Icons.Filled.Person)
)

@Composable
fun HomeScreen(onCerrarSesion: () -> Unit) {
    val tabNav = rememberNavController()
    val backStack by tabNav.currentBackStackEntryAsState()
    val rutaActual = backStack?.destination?.route

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
            composable(Routes.TAB_MAPA) { MapaScreen(onPerfil = { irA(Routes.TAB_PERFIL) }) }
            composable(Routes.TAB_RUTAS) { PlaceholderTab("Rutas patrimoniales") }
            composable(Routes.TAB_AVISOS) { PlaceholderTab("Avisos") }
            composable(Routes.TAB_PERFIL) { PerfilScreen(onCerrarSesion) }
        }

        BarraFlotante(
            rutaActual = rutaActual,
            onTabClick = irA,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun BarraFlotante(
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