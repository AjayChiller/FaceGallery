package com.example.facegallery.ui.detail


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facegallery.R
import com.example.facegallery.data.model.Faces
import com.example.facegallery.data.model.Photo
import com.example.facegallery.ui.detail.compose.AddNamesBottomSheet
import com.example.facegallery.ui.utils.OverlayView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


import kotlinx.coroutines.launch
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotoDetailCompose(photo: Photo, viewModel: PhotoDetailViewModel = viewModel()) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val uri = Uri.parse(photo.uri)

    val gson = Gson()

    val _faces: List<Faces> = gson.fromJson(photo.facesList, object : TypeToken<List<Faces?>?>() {}.getType())
    val faces = remember { mutableStateListOf<Faces>() }
    faces.addAll(_faces)

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uri) {
        // Load the bitmap on a background thread
        bitmap = withContext(Dispatchers.IO) {
            loadImageBitmap(uri, context.contentResolver)
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            AddNamesBottomSheet(
                faces = faces,
                onDismiss = {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                },
                onUpdateNames = { updatedFaces ->
                    faces.clear()
                    faces.addAll(updatedFaces)
                    photo.facesList = Gson().toJson(faces.toList())
                    viewModel.updatePhoto(photo)
                }
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(
                    onAddNamesClick = {
                        coroutineScope.launch {
                            bottomSheetState.show()  // Show the bottom sheet when button is clicked
                        }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentScale = ContentScale.FillWidth
                    )

                    // Custom Overlay View in Compose
                    AndroidView(
                        factory = { context ->
                            OverlayView(context, null).apply {
                                viewTreeObserver.addOnGlobalLayoutListener {
                                    setResults(faces, it.height, it.width)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Composable
fun BottomBar(onAddNamesClick: () -> Unit) {
    Button(
        colors  = ButtonColors(
            contentColor = Color.White,
            containerColor = colorResource(R.color.primary_color),
            disabledContainerColor = colorResource(R.color.primary_color),
            disabledContentColor = Color.Black
        ),

                onClick = onAddNamesClick,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(text = "Add Names", style = MaterialTheme.typography.button)
    }
}


suspend fun loadImageBitmap(uri: Uri, contentResolver: android.content.ContentResolver): Bitmap? {
    return withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }?.copy(Bitmap.Config.ARGB_8888, true)
    }
}
