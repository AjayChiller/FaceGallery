package com.example.facegallery.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.facegallery.R
import com.example.facegallery.data.Faces
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
import kotlin.math.min

class OverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    private var results: List<Faces>? = listOf()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var scaleFactor: Float = 1f

    private var bounds = Rect()

    init {
        initPaints()
    }

    fun clear() {
        results = null
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = ContextCompat.getColor(context!!, R.color.mp_primary)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }

    @SuppressLint("DefaultLocale")
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        Log.d("DDDD","draw  - "+(results == null))
        results?.let { it ->

            for (detection in it) {
                val boundingBox = detection.rect

                val top = boundingBox.top * scaleFactor
                val bottom = boundingBox.bottom * scaleFactor
                val left = boundingBox.left * scaleFactor
                val right = boundingBox.right * scaleFactor

                // Draw bounding box around detected faces
                val drawableRect = RectF(left, top, right, bottom)
                canvas.drawRect(drawableRect, boxPaint)


                // Create text to display alongside detected faces
                val drawableText = "name:score"
               //     detection.categories()[0].categoryName() +



                // Draw rect behind display text
                textBackgroundPaint.getTextBounds(
                    drawableText,
                    0,
                    drawableText.length,
                    bounds
                )
                val textWidth = bounds.width()
                val textHeight = bounds.height()
                canvas.drawRect(
                    left,
                    top,
                    left + textWidth + BOUNDING_RECT_TEXT_PADDING,
                    top + textHeight + BOUNDING_RECT_TEXT_PADDING,
                    textBackgroundPaint
                )

                // Draw text for detected face
                canvas.drawText(
                    drawableText,
                    left,
                    top + bounds.height(),
                    textPaint
                )
            }
        }
    }

    fun setResults(
        facesJson: String,
        imageHeight: Int,
        imageWidth: Int,
    ) {
        val faces: List<Faces> = Gson().fromJson(
            facesJson,
            object : TypeToken<List<Faces?>?>() {}.getType()
        )


        results = faces
       // Log.d("DDDD","draw results?.detections()  - "+(results?.detections()?.size))
        // Images, videos and camera live streams are displayed in FIT_START mode. So we need to scale
        // up the bounding box to match with the size that the images/videos/live streams being
        // displayed.
        scaleFactor = min(width * 1f / imageWidth, height * 1f / imageHeight)

        invalidate()
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}