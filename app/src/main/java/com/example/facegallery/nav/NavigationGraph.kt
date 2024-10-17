package com.example.facegallery.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.facegallery.ui.FullScreenImage
import com.example.facegallery.ui.GalleryScreen
import com.example.facegallery.ui.GalleryViewModel

@Composable
fun NavigationGraph(navController: NavHostController, galleryViewModel: GalleryViewModel) {
    NavHost(navController, startDestination = "gallery") {
        composable("gallery") {
       //     GalleryScreen(viewModel = galleryViewModel, navController = navController)
        }
        composable("full_screen/{photo}") {   backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")
            FullScreenImage(
                navController = navController,
                viewModel = galleryViewModel,
            )
        }
    }
}
