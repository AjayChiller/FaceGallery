package com.example.facegallery.ui.gallery



import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.facegallery.data.model.Photo
import com.example.facegallery.data.repository.GalleryRepository
import kotlinx.coroutines.launch

class GalleryViewModel(context: Context) : ViewModel() {
    private val repository = GalleryRepository(context)
    var isPermissionGranted = mutableStateOf(false)
    val countState = repository.count.asLiveData()
    val isProcessingComplete = repository.isProcessingComplete.asLiveData()

    val photos: LiveData<List<Photo>> = repository.allPhotos

    fun loadPhotosFromDevice() {
        viewModelScope.launch {
            repository.loadPhotosFromDevice()
        }
    }

}
