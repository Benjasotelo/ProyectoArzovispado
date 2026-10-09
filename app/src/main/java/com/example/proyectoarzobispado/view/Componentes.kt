package com.example.proyectoarzobispado.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.proyectoarzobispado.ui.theme.Terracota

@Composable
fun LogoApp(size: Dp = 72.dp) {
    val forma = RoundedCornerShape(22.dp)
    Box(
        modifier = Modifier
            .size(size)
            .shadow(8.dp, forma, ambientColor = Terracota, spotColor = Terracota)
            .background(Terracota, forma),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.AccountBalance,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}
