package com.example.proyectoarzobispado.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectoarzobispado.ui.theme.Terracota
import com.example.proyectoarzobispado.ui.theme.TextoOscuro
import com.example.proyectoarzobispado.viewmodel.EscanerViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@Composable
fun EscanerScreen(
    onVolver: () -> Unit,
    onRecintoEncontrado: (Int) -> Unit,
    vm: EscanerViewModel = viewModel()
) {
    val context = LocalContext.current

    var permisoCamara by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido -> permisoCamara = concedido }

    // Pide el permiso al entrar si aún no lo tiene
    LaunchedEffect(Unit) {
        if (!permisoCamara) launcher.launch(Manifest.permission.CAMERA)
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        if (permisoCamara) {
            VistaCamara(onTextoLeido = { texto -> vm.procesar(texto, onRecintoEncontrado) })

            // Marco guía para apuntar al QR
            Box(
                Modifier
                    .align(Alignment.Center)
                    .size(240.dp)
                    .border(3.dp, Terracota, RoundedCornerShape(24.dp))
            )
            Text(
                "Apunta al código QR del templo",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Center).offset(y = 150.dp)
            )
        } else {
            Column(
                modifier = Modifier.align(Alignment.Center).padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Necesitamos la cámara para leer el código QR del templo.",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = { launcher.launch(Manifest.permission.CAMERA) },
                    colors = ButtonDefaults.buttonColors(containerColor = Terracota)
                ) {
                    Text("Dar permiso")
                }
            }
        }

        IconButton(
            onClick = onVolver,
            modifier = Modifier.align(Alignment.TopStart).statusBarsPadding().padding(8.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
        }

        // Mensaje cuando el QR no es válido
        vm.mensaje?.let { texto ->
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 24.dp, end = 24.dp, bottom = 130.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Text(texto, modifier = Modifier.padding(16.dp), color = TextoOscuro)
            }
        }
    }
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
private fun VistaCamara(onTextoLeido: (String) -> Unit) {
    // Dentro del NavHost, este owner es la propia pantalla: la cámara se apaga al salir de ella
    val lifecycleOwner = LocalLifecycleOwner.current

    // Lector de ML Kit configurado solo para códigos QR
    val lector = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )
    }
    DisposableEffect(Unit) { onDispose { lector.close() } }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            val vista = PreviewView(ctx)
            val futuroProveedor = ProcessCameraProvider.getInstance(ctx)

            futuroProveedor.addListener({
                val proveedor = futuroProveedor.get()

                // 1) Vista previa: lo que ve la cámara
                val vistaPrevia = Preview.Builder().build()
                vistaPrevia.setSurfaceProvider(vista.surfaceProvider)

                // 2) Análisis: cada imagen se pasa a ML Kit
                val analisis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                analisis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imagenProxy ->
                    val imagen = imagenProxy.image
                    if (imagen == null) {
                        imagenProxy.close()
                        return@setAnalyzer
                    }
                    val entrada = InputImage.fromMediaImage(
                        imagen,
                        imagenProxy.imageInfo.rotationDegrees
                    )
                    lector.process(entrada)
                        .addOnSuccessListener { codigos ->
                            codigos.firstNotNullOfOrNull { it.rawValue }?.let(onTextoLeido)
                        }
                        .addOnCompleteListener { imagenProxy.close() }
                }

                // 3) Conecta ambos a la cámara trasera y al ciclo de vida de la pantalla
                proveedor.unbindAll()
                proveedor.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    vistaPrevia,
                    analisis
                )
            }, ContextCompat.getMainExecutor(ctx))

            vista
        }
    )
}