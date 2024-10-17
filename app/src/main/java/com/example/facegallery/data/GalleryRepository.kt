package com.example.facegallery.data

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
import com.example.facegallery.database.AppDatabase
import com.example.facegallery.database.Photo
import com.example.facegallery.database.PhotoDao
import com.example.facegallery.facedetector.FaceDetectorHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.mediapipe.tasks.vision.core.RunningMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService


class GalleryRepository(private val context: Context) {
    private val photoDao: PhotoDao = AppDatabase.getDatabase(context).photoDao()

    val allPhotos: LiveData<List<Photo>> = photoDao.getAllPhotos()
    var existingPhotos = listOf<String>()



    suspend fun loadPhotos() {
        withContext(Dispatchers.IO) {
            existingPhotos = photoDao.getAllPhotosNow().map { it.uri }
            if (faceDetectorHelper == null) {
                initFaceDetectorHelper()
            }
            val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED)
            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )

            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateAddedColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                Log.e("DDDD", "existingPhotos List size" + existingPhotos.size)
                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon()
                        .appendPath(id.toString()).build().toString()
                    Log.d("DDDD","uri ----- > "+uri)
                    val timestamp = it.getLong(dateAddedColumn)
                    runDetectionOnImage(uri, timestamp)
                }
            }
        }

    }

    val listener = object :  FaceDetectorHelper.DetectorListener  {
        override fun onError(error: String, errorCode: Int) {
            detectError()

                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
//                if (errorCode == FaceDetectorHelper.GPU_ERROR) {
//                    fragmentGalleryBinding.bottomSheetLayout.spinnerDelegate.setSelection(
//                        FaceDetectorHelper.DELEGATE_CPU, false
//                    )
//                }

        }

        override fun onResults(resultBundle: FaceDetectorHelper.ResultBundle) {
            TODO("Not yet implemented")
        }

    }

    private fun detectError() {
//        (context as Activity).runOnUiThread {
//            fragmentGalleryBinding.progress.visibility = View.GONE
//            setUiEnabled(true)
//            updateDisplayView(MediaType.UNKNOWN)
//        }
    }



    private lateinit var backgroundExecutor: ScheduledExecutorService
    private var faceDetectorHelper: FaceDetectorHelper? = null

    private fun initFaceDetectorHelper() {
        faceDetectorHelper = FaceDetectorHelper(
            context = context,
            threshold = FaceDetectorHelper.THRESHOLD_DEFAULT,
            currentDelegate = FaceDetectorHelper.DELEGATE_CPU,
            runningMode = RunningMode.IMAGE,
            faceDetectorListener = listener
        )
    }
    @SuppressLint("DefaultLocale")
    fun runDetectionOnImage(uri: String, timestamp: Long) {
        val imageUri = Uri.parse(uri)
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()
        val contentResolver: ContentResolver = context.contentResolver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(
                contentResolver,
                imageUri
            )
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(
                contentResolver,
                imageUri
            )
        }
            .copy(Bitmap.Config.ARGB_8888, true)
            ?.let { bitmap ->

                // Run face detection on the input image
                backgroundExecutor.execute {
                    faceDetectorHelper?.detectImage(bitmap)?.let { resultBundle ->
                            (context as Activity).runOnUiThread {
                                CoroutineScope(Dispatchers.IO).launch {
                                    if (uri !in existingPhotos) {
                                        val isFaceDetected = (resultBundle.results[0].detections().size > 0)
                                        val faceList: ArrayList<Faces> = arrayListOf()
                                        if (isFaceDetected) {
                                            for (item in resultBundle.results[0].detections()) {
                                                val face = Faces("", item.boundingBox())
                                                faceList.add(face)
                                            }
                                        }
                                        val json = Gson().toJson(faceList.toList())
                                        try {
                                            photoDao.insert(
                                                Photo(
                                                    uri = uri,
                                                    timestamp = timestamp,
                                                    isFaceDetected = isFaceDetected,
                                                    facesList = json,
                                                    width = bitmap.width,
                                                    height = bitmap.height
                                                )
                                            )
                                        } catch (e: Exception) {
                                            Log.d("DDDD","Exception -- "+e.message)
                                        }
                                    } else {
                                        Log.e("DDDD"," already in DB"+uri)

                                    }
                                }
                            }
                        } ?: run {
                        Log.e("TAG", "Error running face detection.")
                    }

                  //  faceDetectorHelper?.clearFaceDetector()
                }
            }
    }
}
