package com.example.facegallery.database

import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val timestamp: Long,
    val isFaceDetected: Boolean = false,
    val facesList: String,
    val width: Int,
    val height: Int,
)