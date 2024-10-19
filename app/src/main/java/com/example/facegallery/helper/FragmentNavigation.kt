package com.example.facegallery.helper

import com.example.facegallery.data.model.Photo

interface FragmentNavigation {
    fun navigateToFullScreen(photo: Photo)
}