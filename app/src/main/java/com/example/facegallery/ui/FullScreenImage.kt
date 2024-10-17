package com.example.facegallery.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.facegallery.facedetector.FaceDetectorHelper
import com.example.facegallery.util.OverlayView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
import java.lang.reflect.Type

@Composable
fun FullScreenImage(
    navController: NavController,
    viewModel: GalleryViewModel
) {
    val photo = viewModel.selectedPhoto


    Box(modifier = Modifier.fillMaxSize()) {
        // Load the image using Coil's AsyncImage
        AsyncImage(
            model = photo?.uri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )




//        AndroidView(factory = { context ->
//            OverlayView(context, null ).apply {
//             setResults(a, photo?.width?:0, photo?.height?: 0)
//            }
//        })

        // Close button to pop back to the previous screen
//        IconButton(
//            onClick = { navController.popBackStack() },
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text("Close", style = MaterialTheme.typography.bodyLarge)
//        }
    }
}
