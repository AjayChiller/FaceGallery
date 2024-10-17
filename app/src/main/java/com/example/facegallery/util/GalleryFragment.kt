/*
 * Copyright 2023 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.mediapipe.examples.facedetection.fragments

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.facegallery.database.Photo
import com.example.facegallery.databinding.FragmentGalleryBinding
import com.example.facegallery.facedetector.FaceDetectorHelper
import com.example.facegallery.util.MainViewModel
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class GalleryFragment(val photo: Photo) : Fragment(), FaceDetectorHelper.DetectorListener {

    private var _fragmentGalleryBinding: FragmentGalleryBinding? = null
    private val fragmentGalleryBinding
        get() = _fragmentGalleryBinding!!
    private lateinit var faceDetectorHelper: FaceDetectorHelper

    /** Blocking ML operations are performed using this executor */
    private lateinit var backgroundExecutor: ScheduledExecutorService
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentGalleryBinding =
            FragmentGalleryBinding.inflate(inflater, container, false)

        runDetectionOnImage(Uri.parse(photo.uri))

        return fragmentGalleryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    // Update the values displayed in the bottom sheet. Reset detector.
    private fun updateControlsUi() {
        fragmentGalleryBinding.imageResult.visibility = View.GONE
        fragmentGalleryBinding.overlay.clear()


        fragmentGalleryBinding.overlay.clear()

    }

    // Load and display the image.
    private fun runDetectionOnImage(uri: Uri) {

        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()
        updateDisplayView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(
                requireActivity().contentResolver,
                uri
            )
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                uri
            )
        }
            .copy(Bitmap.Config.ARGB_8888, true)
            ?.let { bitmap ->
                Log.d("XXXXX","img result "+bitmap)
                fragmentGalleryBinding.imageResult.setImageBitmap(bitmap)
                // Run face detection on the input image
                fragmentGalleryBinding.overlay.setResults(
                    photo.facesList,
                    bitmap.height,
                    bitmap.width
                )

            }
    }



    private fun updateDisplayView() {
        fragmentGalleryBinding.overlay.clear()
        fragmentGalleryBinding.imageResult.visibility = View.VISIBLE

    }



    private fun detectError() {
        activity?.runOnUiThread {

            updateDisplayView()
        }
    }

    override fun onError(error: String, errorCode: Int) {
        detectError()
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            if (errorCode == FaceDetectorHelper.GPU_ERROR) {

            }
        }
    }

    override fun onResults(resultBundle: FaceDetectorHelper.ResultBundle) {
        // no-op
    }

    companion object {
        private const val TAG = "GalleryFragment"

        // Value used to get frames at specific intervals for inference (e.g. every 300ms)
        private const val VIDEO_INTERVAL_MS = 300L
    }
}