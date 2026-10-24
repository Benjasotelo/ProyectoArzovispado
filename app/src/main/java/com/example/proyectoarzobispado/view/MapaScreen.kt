package com.example.proyectoarzobispado.view

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.Polyline
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.model.EstadoRecinto
import com.example.proyectoarzobispado.model.Recinto
import com.example.proyectoarzobispado.ui.theme.BordeSuave
import com.example.proyectoarzobispado.ui.theme.Durazno
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.HomeViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner

// ---------- Utilidades ----------

private fun tienePermiso(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

internal fun coloresEstado(estado: EstadoRecinto): Pair<Color, Color> = when (estado) {
    EstadoRecinto.ABIERTO -> Color(0xFFCFEFD6) to Color(0xFF1E5E34)
    EstadoRecinto.CERRADO -> Color(0xFFF6D3CF) to Color(0xFF8A2A22)
    EstadoRecinto.EN_CELEBRACION -> Color(0xFFFBE7B8) to Color(0xFF7A5200)
}

private fun hueDe(estado: EstadoRecinto): Float = when (estado) {
    EstadoRecinto.ABIERTO -> BitmapDescriptorFactory.HUE_GREEN
    EstadoRecinto.CERRADO -> BitmapDescriptorFactory.HUE_RED
    EstadoRecinto.EN_CELEBRACION -> BitmapDescriptorFactory.HUE_ORANGE
}

// Caminando a ~5 km/h (≈ 83 m/min). Si está lejos, muestra km.
private fun textoDistancia(metros: Float): String =
    if (metros >= 5000f) "A ${"%.1f".format(metros / 1000f)} km"
    else "A ${(metros / 83f).toInt().coerceAtLeast(1)} min caminando"

private fun distanciaMetros(desde: LatLng, recinto: Recinto): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        desde.latitude, desde.longitude,
        recinto.latitud, recinto.longitud,
        resultado
    )
    return resultado[0]
}

// ---------- Pantalla ----------

@SuppressLint("MissingPermission")
@Composable
fun MapaScreen(onPerfil: () -> Unit, onAbrirFicha: (Int) -> Unit, onEscanear: () -> Unit, vm: HomeViewModel = viewModel()) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val recinto = vm.recintoSeleccionado

    var permiso by remember { mutableStateOf(tienePermiso(context)) }
    var ubicacion by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-33.4420, -70.6560), 13f)
    }

    fun obtenerUbicacion(centrar: Boolean) {
        LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    val punto = LatLng(loc.latitude, loc.longitude)
                    ubicacion = punto
                    if (centrar) {
                        scope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(punto, 15f))
                        }
                    }
                }
            }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado -> permiso = resultado.values.any { it } }

    // Pide el permiso al entrar
    LaunchedEffect(Unit) {
        if (!permiso) {
            launcher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    // Con permiso, obtiene la ubicación (sin mover el mapa) para calcular distancias
    LaunchedEffect(permiso) { if (permiso) obtenerUbicacion(centrar = false) }

    // Al seleccionar un templo, centra la cámara en él
    LaunchedEffect(recinto?.id) {
        recinto?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLng(LatLng(it.latitud, it.longitud))
            )
        }
    }

    // Cuando hay una ruta activa, encuadra todas sus paradas en el mapa
    LaunchedEffect(vm.rutaEnMapa?.id) {
        val puntos = vm.puntosRuta
        if (puntos.size >= 2) {
            val bounds = LatLngBounds.builder().apply {
                puntos.forEach { include(LatLng(it.latitud, it.longitud)) }
            }.build()
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds, 150)
            )
        }
    }

    Box(Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            contentPadding = PaddingValues(bottom = 110.dp),
            properties = MapProperties(isMyLocationEnabled = permiso),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                mapToolbarEnabled = false
            ),
            onMapClick = { vm.cerrarFicha() }
        ) {
            vm.recintosFiltrados.forEach { r ->
                key(r.id) {
                    Marker(
                        state = rememberMarkerState(position = LatLng(r.latitud, r.longitud)),
                        title = r.nombreOficial,
                        icon = BitmapDescriptorFactory.defaultMarker(hueDe(r.estado)),
                        onClick = { vm.seleccionarRecinto(r); true }
                    )
                }
            }
            // Trazado de la ruta activa (CU-08)
            val puntos = vm.puntosRuta
            if (puntos.size >= 2) {
                Polyline(
                    points = puntos.map { LatLng(it.latitud, it.longitud) },
                    color = Terracota,
                    width = 14f
                )
            }
        }

        // Buscador + filtros (o resultados mientras se escribe)
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Buscador(
                consulta = vm.consulta,
                onConsultaChange = { vm.actualizarConsulta(it) },
                iniciales = vm.iniciales,
                onPerfil = onPerfil
            )
            if (vm.consulta.isNotBlank()) {
                ResultadosBusqueda(
                    resultados = vm.resultados,
                    onElegir = { r ->
                        focusManager.clearFocus()
                        vm.elegirResultado(r)
                        onAbrirFicha(r.id)
                    }
                )
            } else {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EstadoRecinto.entries.forEach { estado ->
                        ChipEstado(
                            estado = estado,
                            seleccionado = vm.filtroEstado == estado,
                            onClick = { vm.seleccionarFiltro(estado) }
                        )
                    }
                }
            }
        }
        // Botón de ubicación + tarjeta del templo
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Durazno)
                    .clickable(onClick = onEscanear),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.QrCodeScanner, "Escanear código QR", tint = Terracota)
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Durazno)
                    .clickable {
                        if (permiso) {
                            obtenerUbicacion(centrar = true)
                        } else {
                            launcher.launch(arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ))
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.MyLocation, "Mi ubicación", tint = Terracota)
            }

            recinto?.let { r ->
                TarjetaRecinto(
                    recinto = r,
                    textoDistancia = ubicacion?.let { textoDistancia(distanciaMetros(it, r)) },
                    onVerFicha = { onAbrirFicha(r.id)  }
                )
            }
        }
    }
}

@Composable
private fun Buscador(
    consulta: String,
    onConsultaChange: (String) -> Unit,
    iniciales: String,
    onPerfil: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Filled.Search, null, tint = TextoOscuro.copy(alpha = 0.7f))
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (consulta.isEmpty()) {
                    Text("Buscar templos o barrios", color = TextoOscuro.copy(alpha = 0.6f))
                }
                BasicTextField(
                    value = consulta,
                    onValueChange = onConsultaChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(color = TextoOscuro, fontSize = 16.sp),
                    cursorBrush = SolidColor(Terracota),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                )
            }
            if (consulta.isNotEmpty()) {
                IconButton(
                    onClick = { onConsultaChange("") },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Borrar búsqueda",
                        tint = TextoOscuro.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Durazno)
                    .clickable(onClick = onPerfil),
                contentAlignment = Alignment.Center
            ) {
                if (iniciales.isNotEmpty()) {
                    Text(iniciales, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Terracota)
                } else {
                    Icon(Icons.Filled.Person, contentDescription = "Perfil", tint = Terracota)
                }
            }
        }
    }
}

@Composable
private fun ResultadosBusqueda(resultados: List<Recinto>, onElegir: (Recinto) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        if (resultados.isEmpty()) {
            Text(
                "Sin resultados",
                modifier = Modifier.padding(16.dp),
                color = TextoOscuro.copy(alpha = 0.7f)
            )
        } else {
            Column {
                resultados.forEachIndexed { indice, r ->
                    if (indice > 0) HorizontalDivider(color = BordeSuave)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onElegir(r) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(r.nombreOficial, fontWeight = FontWeight.Medium, color = TextoOscuro)
                            Text(
                                r.direccion,
                                fontSize = 13.sp,
                                color = TextoOscuro.copy(alpha = 0.7f)
                            )
                        }
                        EstadoTag(r.estado)
                    }
                }
            }
        }
    }
}
@Composable
internal fun ChipEstado(estado: EstadoRecinto, seleccionado: Boolean, onClick: () -> Unit) {
    val (fondo, texto) = coloresEstado(estado)
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (seleccionado) fondo else Color.White)
            .border(1.dp, if (seleccionado) fondo else BordeSuave, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(texto))
        Text(
            estado.etiqueta,
            fontSize = 13.sp,
            color = if (seleccionado) texto else TextoOscuro
        )
    }
}

@Composable
internal fun EstadoTag(estado: EstadoRecinto) {
    val (fondo, texto) = coloresEstado(estado)
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(fondo)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(Modifier.size(7.dp).clip(CircleShape).background(texto))
        Text(estado.etiqueta, fontSize = 12.sp, color = texto)
    }
}

@Composable
private fun TarjetaRecinto(
    recinto: Recinto,
    textoDistancia: String?,
    onVerFicha: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = textoDistancia ?: recinto.tipoRecinto,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Terracota
                )
                EstadoTag(recinto.estado)
            }
            Text(
                recinto.nombreOficial,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
            Text(
                recinto.direccion,
                style = MaterialTheme.typography.bodySmall,
                color = TextoOscuro.copy(alpha = 0.7f)
            )
            Button(
                onClick = onVerFicha,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Durazno,
                    contentColor = Terracota
                )
            ) {
                Text("Ver ficha patrimonial", fontWeight = FontWeight.Medium)
            }
        }
    }
}