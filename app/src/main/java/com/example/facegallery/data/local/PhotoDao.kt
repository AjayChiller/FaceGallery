package com.example.facegallery.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.facegallery.data.model.Photo

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: Photo)

    @Query("SELECT * FROM photos WHERE isFaceDetected = 1 ORDER BY timestamp DESC")
    fun getAllPhotos(): LiveData<List<Photo>>

    @Query("SELECT * FROM photos WHERE isFaceDetected = 1 ORDER BY timestamp DESC")
    fun getAllPhotosNow(): List<Photo>

    @Update
    suspend fun update(photo: Photo)
}