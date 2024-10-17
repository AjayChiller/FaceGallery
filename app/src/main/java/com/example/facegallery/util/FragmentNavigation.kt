package com.example.facegallery.util

import com.example.facegallery.database.Photo

interface FragmentNavigation {
    fun navigateToFullScreen(photo: Photo)
}