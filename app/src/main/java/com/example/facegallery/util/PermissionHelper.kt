package com.example.facegallery.util

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

class PermissionHelper(
    val activity: ComponentActivity,
    val listener: PermissionResultListner
) {

    var permissionsList: ArrayList<String>? = null

    // Update permission array to include Android 13/14 media permissions
    var permissionsStr = arrayOf(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    var permissionsCount = 0
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    fun permissionHelper() {
        permissionsLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result ->
                val list = ArrayList(result.values)
                permissionsList = ArrayList()
                permissionsCount = 0
                for (i in list.indices) {
                    if (shouldShowRequestPermissionRationale(activity, permissionsStr[i])) {
                        permissionsList!!.add(permissionsStr[i])
                    } else if (!hasPermission(permissionsStr[i])) {
                        permissionsCount++
                    }
                }

                if (permissionsList?.size!! > 0) {
                    // Some permissions are denied and can be asked again.
                    askForPermissions(permissionsList!!)
                } else if (permissionsCount > 0) {
                    // Show alert dialog
                    showPermissionDialog()
                } else {
                    listener.onPermissionGranted()
                }
            }
    }

    fun requestPermission() {
        permissionsList = ArrayList()
        permissionsList!!.addAll(permissionsStr)
        permissionHelper()
        askForPermissions(permissionsList!!)
    }

    fun checkIsPermissionGranted(): Boolean {
        Log.d("DDDD", "check $permissionsStr")
        for (permission in permissionsStr) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            } else {
                return false
            }
        }
        return true
    }

    private fun hasPermission(permissionStr: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permissionStr
        ) == PackageManager.PERMISSION_GRANTED
    }

    var alertDialog: AlertDialog? = null
    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Permission required")
            .setMessage("Some permissions are needed to be allowed to use this app without any problems.")
            .setPositiveButton("Grant Permission") { _: DialogInterface, _: Int ->
                // Action to perform when OK is clicked
                openSettingsForPermission(activity)
            }
        if (alertDialog == null) {
            alertDialog = builder.create()
            if (!alertDialog!!.isShowing) {
                alertDialog!!.show()
            }
        } else {
            alertDialog!!.show()
        }
    }

    private fun askForPermissions(list: ArrayList<String>) {
        val newPermissionStr: Array<String?> = arrayOfNulls(list.size)
        for (i in newPermissionStr.indices) {
            newPermissionStr[i] = this.permissionsList?.get(i)
        }

        if (newPermissionStr.isNotEmpty()) {
            // Request the permissions
            permissionsLauncher.launch(newPermissionStr as Array<String>)
        } else {
            // If the user has denied and chosen "Don't ask again"
            showPermissionDialog()
        }
    }

    fun openSettingsForPermission(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivity(intent)
    }
}
