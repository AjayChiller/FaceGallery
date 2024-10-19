package com.example.facegallery

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen(navigateToNextScreen = {
                startActivity(Intent(this, MainGalleryActivity::class.java))
                finish()
            })
        }
    }


    @Composable
    fun SplashScreen(
        navigateToNextScreen: () -> Unit // Callback to navigate after splash screen
    ) {
        // Lottie animation state
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_lottie))
        val progress by animateLottieCompositionAsState(composition, isPlaying = true)

        // Gradient background colors
        val gradientColors = listOf(
            colorResource(R.color.primary_color),
            colorResource(R.color.secondary_color)
        )

        // Launch effect to delay and navigate to the next screen
        LaunchedEffect(key1 = true) {
            delay(2000L) // 2 seconds delay
            navigateToNextScreen()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradientColors)),
            contentAlignment = Alignment.Center
        ) {
            // Lottie Animation
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(300.dp)
            )
        }
    }

    @Preview
    @Composable
    fun SplashScreenPreview() {
        SplashScreen(navigateToNextScreen = {
            if (true) {
                startActivity(Intent(this, MainGalleryActivity::class.java))
            }
            finish()
        })
    }
}

