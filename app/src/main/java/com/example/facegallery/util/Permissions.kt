package com.example.facegallery.util

import android.Manifest.permission.*


sealed class Permissions(vararg val permissions: String) {
    // Individual permissions
    object Camera : Permissions(CAMERA)
    // Bundled permissions
    object ImagePick : Permissions(*getImagePickPermissions())
    object ImgVidCamPerm : Permissions(*getImgVidCamPermission())
    object ImgVidPerm : Permissions(*getImgVidPermission())
    object AudioPickPerm : Permissions(*getAudioPermission())
    // Grouped permissions
    object Location : Permissions(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    companion object {
        private fun getImagePickPermissions(): Array<String> {
            return if (PermissionManager.sdkEqOrAbove33()) {
                arrayOf(READ_MEDIA_IMAGES)
            } else if (PermissionManager.sdkEqOrAbove29()) {
                arrayOf(READ_EXTERNAL_STORAGE)
            } else {
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                  //  WRITE_EXTERNAL_STORAGE
                )
            }
        }
        private fun getImgVidCamPermission(): Array<String> {
            return if (PermissionManager.sdkEqOrAbove33()) {
                arrayOf(READ_MEDIA_IMAGES)
            } else if (PermissionManager.sdkEqOrAbove29()) {
                arrayOf(READ_EXTERNAL_STORAGE)
            } else {
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                )
            }
        }
        private fun getAudioPermission(): Array<String> {
            return if (PermissionManager.sdkEqOrAbove33()) {
                arrayOf(READ_MEDIA_AUDIO)
            } else if (PermissionManager.sdkEqOrAbove29()) {
                arrayOf(READ_EXTERNAL_STORAGE)
            } else {
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                )
            }
        }
        private fun getImgVidPermission(): Array<String> {
            return if (PermissionManager.sdkEqOrAbove33()) {
                arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO)
            } else if (PermissionManager.sdkEqOrAbove29()) {
                arrayOf(READ_EXTERNAL_STORAGE)
            } else {
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }
}