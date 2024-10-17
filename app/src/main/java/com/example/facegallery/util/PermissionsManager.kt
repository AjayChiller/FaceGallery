package com.example.facegallery.util

import android.Manifest
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.facegallery.MainActivity

class PermissionsManager(private val context: Context) {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    fun requestPermission(onGranted: () -> Unit) {
        permissionLauncher = (context as MainActivity).registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onGranted()
            } else {
                // Handle permission denial
            }
        }
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
