package com.example.facegallery.ui.gallery.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.facegallery.R

@Composable
fun RequestPermissionScreen(onPermissionClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Storage Access Required",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "This app needs access to your photos to proceed.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onPermissionClick()
            },
            colors = ButtonColors(
                contentColor = Color.White,
                containerColor = colorResource(R.color.primary_color),
                disabledContainerColor = colorResource(R.color.primary_color),
                disabledContentColor = Color.Black
            ),
        ) {
            Text(
                text = "Grant Permission",
                color = Color.Black,
            )
        }
    }
}

