package com.example.proyectoarzobispado.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** Lo entrega ProyectoArzobispadoTheme: true cuando el teléfono está en modo oscuro. */
val LocalModoOscuro = staticCompositionLocalOf { false }

// ---------- Paleta modo claro ----------
val TerracotaModoClaro = Color(0xFF8B4A3A)
val CremaModoClaro = Color(0xFFFFF8F5)
val DuraznoModoClaro = Color(0xFFFFDACF)
val TextoModoClaro = Color(0xFF2B1A17)
val BordeModoClaro = Color(0xFFE6D5CF)
val SuperficieModoClaro = Color(0xFFFFFFFF)
val SobreTerracotaModoClaro = Color(0xFFFFFFFF)
val ErrorTextoModoClaro = Color(0xFF8A2A22)
val ErrorFondoModoClaro = Color(0xFFF6D3CF)

// ---------- Paleta modo oscuro ----------
val TerracotaModoOscuro = Color(0xFFE59B85)
val CremaModoOscuro = Color(0xFF1B1311)
val DuraznoModoOscuro = Color(0xFF4A2E27)
val TextoModoOscuro = Color(0xFFF3E8E4)
val BordeModoOscuro = Color(0xFF3D2D28)
val SuperficieModoOscuro = Color(0xFF281D1A)
val SobreTerracotaModoOscuro = Color(0xFF2B1A17)
val ErrorTextoModoOscuro = Color(0xFFF2B8B0)
val ErrorFondoModoOscuro = Color(0xFF5A2420)

// ---------- Colores que usan las pantallas (cambian solos según el modo) ----------
val Terracota: Color
    @Composable get() = if (LocalModoOscuro.current) TerracotaModoOscuro else TerracotaModoClaro
val Crema: Color
    @Composable get() = if (LocalModoOscuro.current) CremaModoOscuro else CremaModoClaro
val Durazno: Color
    @Composable get() = if (LocalModoOscuro.current) DuraznoModoOscuro else DuraznoModoClaro
val TextoOscuro: Color
    @Composable get() = if (LocalModoOscuro.current) TextoModoOscuro else TextoModoClaro
val BordeSuave: Color
    @Composable get() = if (LocalModoOscuro.current) BordeModoOscuro else BordeModoClaro
val Superficie: Color
    @Composable get() = if (LocalModoOscuro.current) SuperficieModoOscuro else SuperficieModoClaro
val SobreTerracota: Color
    @Composable get() = if (LocalModoOscuro.current) SobreTerracotaModoOscuro else SobreTerracotaModoClaro
val ErrorTexto: Color
    @Composable get() = if (LocalModoOscuro.current) ErrorTextoModoOscuro else ErrorTextoModoClaro
val ErrorFondo: Color
    @Composable get() = if (LocalModoOscuro.current) ErrorFondoModoOscuro else ErrorFondoModoClaro