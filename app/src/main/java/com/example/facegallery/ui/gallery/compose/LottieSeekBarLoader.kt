package com.example.facegallery.ui.gallery.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facegallery.R


@Composable
fun LottieSeekBarLoader(modifier: Modifier, countState: Int?, isProcessingComplete: Boolean?) {
    Column(
        modifier = modifier.background(colorResource(R.color.black_color_70)),
    ) {
        if (isProcessingComplete == false) {
            LottieAnimation(
                modifier = Modifier
                    .height(3.dp)
                    .fillMaxWidth(),
                resourceId = R.raw.bottom_video_loader
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (isProcessingComplete == true) {
                Text(
                    text = "Completed",
                    color = colorResource(id = R.color.white),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            } else {
                Text(
                    text = "Processing...",
                    color = colorResource(id = R.color.white),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Text(
                text = countState.toString(),
                color = colorResource(id = R.color.white),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}