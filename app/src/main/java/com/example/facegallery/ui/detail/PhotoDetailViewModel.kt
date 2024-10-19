/*
 * Copyright 2023 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.facegallery.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facegallery.data.model.Photo
import com.example.facegallery.data.repository.GalleryRepository
import com.example.facegallery.helper.FaceDetectorHelper
import kotlinx.coroutines.launch

/**
 *  This ViewModel is used to store face detector helper settings
 */
class PhotoDetailViewModel(context: Context) : ViewModel() {
    private val repository = GalleryRepository(context)

    fun updatePhoto(photo: Photo) {
        viewModelScope.launch {
            repository.updatePhoto(photo)
        }
    }

}