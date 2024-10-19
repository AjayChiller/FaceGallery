package com.example.facegallery.helper

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.example.facegallery.ui.gallery.GalleryViewModel


class MediaStoreObserver(private val context: Context, private val viewModel: GalleryViewModel) : ContentObserver(Handler(Looper.getMainLooper())) {
    override fun onChange(self: Boolean) {
        super.onChange(self)
        viewModel.loadPhotosFromDevice() // Refresh photos when the media store changes
    }

    fun register() {
        context.contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            this
        )
    }

    fun unregister() {
        context.contentResolver.unregisterContentObserver(this)
    }
}
