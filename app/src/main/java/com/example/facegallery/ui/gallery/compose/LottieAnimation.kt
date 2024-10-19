package com.example.facegallery.ui.gallery.compose

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieAnimation(modifier: Modifier = Modifier, @RawRes resourceId: Int) {
    val rawComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(resourceId))
    com.airbnb.lottie.compose.LottieAnimation(
        composition = rawComposition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier,
        contentScale = ContentScale.FillBounds,
    )
}