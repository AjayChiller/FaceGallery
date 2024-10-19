package com.example.facegallery.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.facegallery.data.model.Photo
import com.example.facegallery.helper.FaceDetectorHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class PhotoDetailFragment(val photo: Photo) : Fragment(), FaceDetectorHelper.DetectorListener {

    private lateinit var faceDetectorHelper: FaceDetectorHelper
    private lateinit var backgroundExecutor: ScheduledExecutorService
    private lateinit var viewModel: PhotoDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), PhotoDetailViewModelFactory(requireContext())).get(
            PhotoDetailViewModel::class.java)

        faceDetectorHelper = FaceDetectorHelper(context = requireContext(), faceDetectorListener = this)
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PhotoDetailCompose(photo, viewModel, )
            }
        }
    }

    override fun onError(error: String, errorCode: Int) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResults(resultBundle: FaceDetectorHelper.ResultBundle) {
        // Handle face detection results here (no-op for now)
    }
}
