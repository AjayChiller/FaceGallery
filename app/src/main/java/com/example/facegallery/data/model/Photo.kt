package com.example.facegallery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val timestamp: Long,
    val isFaceDetected: Boolean = false,
    var facesList: String,
    val width: Int,
    val height: Int,
)