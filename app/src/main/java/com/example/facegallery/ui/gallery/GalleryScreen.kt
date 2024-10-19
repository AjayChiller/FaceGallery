package com.example.facegallery.ui.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.facegallery.helper.FragmentNavigation
import com.example.facegallery.ui.gallery.compose.LottieSeekBarLoader

@Composable
fun GalleryScreen(viewModel: GalleryViewModel, fragmentNavigation: FragmentNavigation) {
    val photos by viewModel.photos.observeAsState(emptyList())
    val count by viewModel.countState.observeAsState()
    val isProcessingComplete by viewModel.isProcessingComplete.observeAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(photos) { photo ->
                Image(
                    painter = rememberImagePainter(photo.uri),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(1.dp)
                        .size(100.dp)
                        .clickable {
                            fragmentNavigation.navigateToFullScreen(photo)
                        }
                )
            }

        }
        LottieSeekBarLoader(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            count,
            isProcessingComplete
        )
    }
}
