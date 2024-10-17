package com.example.facegallery.util

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.example.facegallery.ui.GalleryViewModel


class MediaStoreObserver(private val context: Context, private val viewModel: GalleryViewModel) : ContentObserver(Handler(Looper.getMainLooper())) {
    override fun onChange(self: Boolean) {
        super.onChange(self)
        viewModel.loadPhotos() // Refresh photos when the media store changes
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
