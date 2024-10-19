package com.example.facegallery.data.repository

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.facegallery.data.model.Faces
import com.example.facegallery.data.local.AppDatabase
import com.example.facegallery.data.model.Photo
import com.example.facegallery.data.local.PhotoDao
import com.example.facegallery.helper.FaceDetectorHelper
import com.google.gson.Gson
import com.google.mediapipe.tasks.vision.core.RunningMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext


class GalleryRepository(private val context: Context) {
    private val TAG: String = GalleryRepository::class.java.simpleName
    private val photoDao: PhotoDao = AppDatabase.getDatabase(context).photoDao()

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count
    private val _isProcessingComplete = MutableStateFlow(false)
    val isProcessingComplete: StateFlow<Boolean> = _isProcessingComplete

    // Increment the count of processed photos
    fun incrementCount() {
        _count.value += 1
    }

    val allPhotos: LiveData<List<Photo>> = photoDao.getAllPhotos()
    private var existingPhotos = listOf<String>()

    suspend fun updatePhoto(photo: Photo) {
        photoDao.update(photo)
    }

    // Load photos from the device and run face detection
    suspend fun loadPhotosFromDevice() {
        withContext(Dispatchers.IO) {
            // Get all existing photos' URIs to avoid duplicates
            existingPhotos = photoDao.getAllPhotosNow().map { it.uri }
            if (faceDetectorHelper == null) {
                initFaceDetectorHelper()
            }

            // Query the device for media images
            val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED)
            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )

            // Iterate over the photos and run face detection
            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateAddedColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon()
                        .appendPath(id.toString()).build().toString()
                    val timestamp = it.getLong(dateAddedColumn)
                    runDetectionOnImage(uri, timestamp)
                }
            }

            _isProcessingComplete.value = true
            faceDetectorHelper?.clearFaceDetector()  // Clear the face detector after processing
        }
    }


    val listener = object : FaceDetectorHelper.DetectorListener {
        override fun onError(error: String, errorCode: Int) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        override fun onResults(resultBundle: FaceDetectorHelper.ResultBundle) {
            //  Handle results here (implementation pending)
        }
    }

    private var faceDetectorHelper: FaceDetectorHelper? = null

    // Initialize the face detection helper with default settings
    private fun initFaceDetectorHelper() {
        faceDetectorHelper = FaceDetectorHelper(
            context = context,
            threshold = FaceDetectorHelper.THRESHOLD_DEFAULT,
            currentDelegate = FaceDetectorHelper.DELEGATE_CPU,
            runningMode = RunningMode.IMAGE,
            faceDetectorListener = listener
        )
    }

    // Run face detection on an image if it hasn't been processed before
    @SuppressLint("DefaultLocale")
    suspend fun runDetectionOnImage(uri: String, timestamp: Long) {
        if (uri !in existingPhotos) {
            Log.d("DDDD", "run detection " + uri + "   " + _count.value)
            val imageUri = Uri.parse(uri)
            val contentResolver: ContentResolver = context.contentResolver

            // Decode the image based on the SDK version
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            }
                .copy(Bitmap.Config.ARGB_8888, true)  // Create a mutable bitmap

            bitmap?.let {
                // Run face detection on the image
                faceDetectorHelper?.detectImage(it)?.let { resultBundle ->
                    val isFaceDetected = (resultBundle.results[0].detections().size > 0)
                    val faceList: ArrayList<Faces> = arrayListOf()

                    // If faces are detected, add them to the list
                    if (isFaceDetected) {
                        val detections = resultBundle.results[0].detections()
                        for (count in 0 until detections.size) {
                            val face = Faces(
                                "Face " + (count + 1),
                                detections[count].boundingBox()
                            )
                            faceList.add(face)
                        }
                    }

                    // Convert face list to JSON
                    val json = Gson().toJson(faceList.toList())

                    // Insert photo data into the database
                    try {
                        photoDao.insert(
                            Photo(
                                uri = uri,
                                timestamp = timestamp,
                                isFaceDetected = isFaceDetected,
                                facesList = json,
                                width = it.width,
                                height = it.height
                            )
                        )
                    } catch (e: Exception) {
                        Log.d(TAG, "Exception adding to DB -- " + e.message)
                    }
                }
                incrementCount()  // Increment processed photo count
            }
        } else {
            incrementCount()  // If the photo already exists, just increment the count
        }
    }
}

