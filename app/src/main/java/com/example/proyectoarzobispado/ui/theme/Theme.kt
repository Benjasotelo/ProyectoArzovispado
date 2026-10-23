package com.example.proyectoarzobispado.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val EsquemaClaro = lightColorScheme(
    primary = TerracotaModoClaro,
    onPrimary = SobreTerracotaModoClaro,
    secondary = DuraznoModoClaro,
    onSecondary = TextoModoClaro,
    background = CremaModoClaro,
    onBackground = TextoModoClaro,
    surface = SuperficieModoClaro,
    onSurface = TextoModoClaro,
    outline = BordeModoClaro
)

private val EsquemaOscuro = darkColorScheme(
    primary = TerracotaModoOscuro,
    onPrimary = SobreTerracotaModoOscuro,
    secondary = DuraznoModoOscuro,
    onSecondary = TextoModoOscuro,
    background = CremaModoOscuro,
    onBackground = TextoModoOscuro,
    surface = SuperficieModoOscuro,
    onSurface = TextoModoOscuro,
    outline = BordeModoOscuro
)

@Composable
fun ProyectoArzobispadoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalModoOscuro provides darkTheme) {
        MaterialTheme(
            colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
            typography = Typography,
            content = content
        )
    }
}