package com.example.facegallery.ui


import android.net.Uri
import android.os.Bundle
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.facegallery.database.Photo
import com.example.facegallery.R
import com.example.facegallery.util.FragmentNavigation
import com.google.gson.Gson
import com.google.mediapipe.examples.facedetection.fragments.GalleryFragment

@Composable
fun GalleryScreen(viewModel: GalleryViewModel, fragmentNavigation: FragmentNavigation) {
    val photos by viewModel.photos.observeAsState(emptyList())
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var isAnimating by remember { mutableStateOf(false) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize()
    ) {
        items(photos) { photo ->
            Image(
                painter = rememberImagePainter(photo.uri),
                contentDescription = null,
                modifier = Modifier
                    .padding(4.dp)
                    .size(100.dp)
                    .clickable {

                        fragmentNavigation.navigateToFullScreen(photo)
                      //  navController.navigate("full_screen/${photo.uri}")
                    }
            )
        }
    }
//    if (isAnimating && selectedImageUri != null) {
//        FullScreenTransition(
//            navController = navController,
//            imageUri = Uri.encode(selectedImageUri), // Encode the URI
//            onAnimationEnd = { isAnimating = false }
//        )
//    }
}

@Composable
fun FullScreenTransition(navController: NavController, imageUri: String?, onAnimationEnd: () -> Unit) {
    // Use a Box to overlay the transition
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Start with the small image
        Image(
            painter = painterResource(R.drawable.ic_launcher_background), // Placeholder image
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .scale(2f) // Scale up for transition
                .animateContentSize() // Animate size change
                .clickable {
                    onAnimationEnd()
                    navController.navigate("full_screen/$imageUri")
                }
        )

        LaunchedEffect(Unit) {
            // Delay for animation before navigating
            kotlinx.coroutines.delay(300) // Adjust for your animation duration
            onAnimationEnd()
            navController.navigate("full_screen/$imageUri")
        }
    }
}

