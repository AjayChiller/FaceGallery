package com.example.facegallery

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import android.Manifest
import android.os.Handler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.facegallery.database.Photo
import com.example.facegallery.ui.GalleryScreen
import com.example.facegallery.ui.GalleryViewModel
import com.example.facegallery.ui.GalleryViewModelFactory
import com.example.facegallery.util.FragmentNavigation
import com.example.facegallery.util.MediaStoreObserver
import com.example.facegallery.util.PermissionManager
import com.example.facegallery.util.Permissions
import com.google.gson.Gson
import com.google.mediapipe.examples.facedetection.fragments.GalleryFragment

class MainActivity : AppCompatActivity(), FragmentNavigation {
    private lateinit var viewModel: GalleryViewModel
    private val permissionManager = PermissionManager.from(this)



    private var mediaStoreObserver: MediaStoreObserver? = null


    private val REQUEST_CODE_PERMISSION = 1001
    private val REQUEST_IMAGE_PICK = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, GalleryViewModelFactory(this)).get(GalleryViewModel::class.java)

        Handler().postDelayed({
            permissionManager
                .request(Permissions.ImgVidCamPerm)
                .rationale(
                    title = "Permission Required",
                    description = "This app needs permission to access your camera",
                ).permissionPermanentlyDeniedContent(
                    description = "You have permanently denied the permission. Please enable it from settings",
                ).checkAndRequestPermission {
                    if (it) {
                        // Show toast or do something
                        showImages()
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                    } else {
                        // Show toast or do something
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
        }, 2000)


        setContent {
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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaStoreObserver?.unregister()
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE_PERMISSION)
    }

    private fun showImages() {
        viewModel.loadPhotos() // Load photos after permission is granted
        mediaStoreObserver = MediaStoreObserver(this, viewModel)
        mediaStoreObserver?.register()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed to open the image picker
                    showImages()
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(this, "Permission denied to access images", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun navigateToFullScreen(photo: Photo) {
        val fragment = GalleryFragment(photo).apply {
            arguments = Bundle().apply {
                putString("photoJson", Gson().toJson(photo))
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}

