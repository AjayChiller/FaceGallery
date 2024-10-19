package com.example.facegallery

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import com.example.facegallery.data.model.Photo
import com.example.facegallery.helper.FragmentNavigation
import com.example.facegallery.ui.gallery.GalleryScreen
import com.example.facegallery.ui.gallery.GalleryViewModel
import com.example.facegallery.ui.gallery.GalleryViewModelFactory
import com.example.facegallery.ui.detail.PhotoDetailFragment
import com.example.facegallery.helper.MediaStoreObserver
import com.example.facegallery.ui.gallery.compose.RequestPermissionScreen
import com.example.facegallery.util.PermissionHelper
import com.example.facegallery.util.PermissionResultListner
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainGalleryActivity : AppCompatActivity(), FragmentNavigation, PermissionResultListner {
    private lateinit var viewModel: GalleryViewModel
    private var mediaStoreObserver: MediaStoreObserver? = null
    var permissionHelper : PermissionHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, GalleryViewModelFactory(this)).get(GalleryViewModel::class.java)

        permissionHelper = PermissionHelper(this, this)
        permissionHelper?.requestPermission()
        setContent {
            if (viewModel.isPermissionGranted.value) {
                GalleryScreen(viewModel = viewModel, this)
                AndroidView(factory = { context ->
                    androidx.fragment.app.FragmentContainerView(context).apply {
                        id = R.id.fragment_container // This ID is arbitrary; we will create it in code
                        layoutParams = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT,
                            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT
                        )
                    }
                })
            } else {
                RequestPermissionScreen(onPermissionClick = {
                    if (permissionHelper?.checkIsPermissionGranted() == true) {
                        onPermissionGranted()
                    } else {
                        permissionHelper?.openSettingsForPermission(this)

                    }
                })
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (permissionHelper?.checkIsPermissionGranted() == true) {
            onPermissionGranted()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaStoreObserver?.unregister()
    }

    private fun loadImagesFromDevice() {
        viewModel.loadPhotosFromDevice() // Load photos after permission is granted
        mediaStoreObserver = MediaStoreObserver(this, viewModel)
        mediaStoreObserver?.register()
    }

    override fun navigateToFullScreen(photo: Photo) {
        val fragment = PhotoDetailFragment(photo).apply {
            arguments = Bundle().apply {
                putString("photoJson", Gson().toJson(photo))
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onPermissionGranted() {
        if (!viewModel.isPermissionGranted.value) {
            viewModel.isPermissionGranted.value = true
            CoroutineScope(Dispatchers.IO).launch {
                loadImagesFromDevice()
            }
        }
    }

}

