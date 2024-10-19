package com.example.facegallery.ui.detail.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.facegallery.R
import com.example.facegallery.data.model.Faces

@Composable
fun AddNamesBottomSheet(
    faces: List<Faces>,
    onDismiss: () -> Unit,
    onUpdateNames: (List<Faces>) -> Unit
) {
    var updatedFaces by remember { mutableStateOf(faces) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Add/Update Face Names", style = MaterialTheme.typography.h6)

        // Create a TextField for each face
        updatedFaces.forEachIndexed { index, face ->
            var name by remember { mutableStateOf(face.name) }

            TextField(
                value = name,
                onValueChange = {
                    name = it
                    updatedFaces = updatedFaces.mapIndexed { idx, f ->
                        if (idx == index) f.copy(name = name) else f
                    }
                },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), // Add padding to ensure visibility
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = colorResource(R.color.primary_color),
                    disabledContainerColor = colorResource(R.color.primary_color),
                    disabledContentColor = Color.Black
                ),
                onClick = {
                    onUpdateNames(updatedFaces)
                    onDismiss()
                },
            ) {

                Text("Save")
            }
        }
    }
}