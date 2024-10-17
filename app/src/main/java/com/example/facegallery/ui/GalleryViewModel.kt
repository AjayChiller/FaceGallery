package com.example.facegallery.ui



import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facegallery.data.GalleryRepository
import com.example.facegallery.database.Photo
import com.example.facegallery.facedetector.FaceDetectorHelper
import com.google.mediapipe.tasks.vision.core.RunningMode
import kotlinx.coroutines.launch

class GalleryViewModel(context: Context) : ViewModel() {
    private val repository = GalleryRepository(context)
    private val faceDetectorHelper = FaceDetectorHelper(
        context = context,
        threshold = FaceDetectorHelper.THRESHOLD_DEFAULT,
        currentDelegate = FaceDetectorHelper.DELEGATE_CPU,
        runningMode = RunningMode.IMAGE
    )
    var selectedPhoto: Photo? = null
    val photos: LiveData<List<Photo>> = repository.allPhotos

    fun loadPhotos() {
        viewModelScope.launch {
            repository.loadPhotos()
        }
    }

}
