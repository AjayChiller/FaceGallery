package com.example.facegallery.ui.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.facegallery.R
import com.example.facegallery.data.model.Faces
import kotlin.math.min


class OverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    private var results: List<Faces>? = null
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
        textBackgroundPaint.color = Color.parseColor("#272727")
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 40f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 30f

        boxPaint.color = ContextCompat.getColor(context!!, R.color.primary_color)
        boxPaint.strokeWidth = 5F
        boxPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        results?.let {
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
                val drawableText = detection.name
                // Measure the size of the text
                textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
                val textWidth = bounds.width()
                val textHeight = bounds.height()

                // Calculate the text position: Bottom-left outside the bounding box
                val textLeft = left
                val textTop = bottom + textHeight + BOUNDING_RECT_TEXT_PADDING // Position below the bounding box



                textBackgroundPaint.color = Color.BLACK
                textBackgroundPaint.alpha = 158 // 50% opacity (255 * 0.5)

                // Draw rounded rect behind the display text (below the bounding box)
                val backgroundRect = RectF(
                    textLeft,
                    bottom + BOUNDING_RECT_TEXT_PADDING, // Just outside the bottom of the rectangle
                    textLeft + textWidth + BOUNDING_RECT_TEXT_PADDING,
                    textTop + BOUNDING_RECT_TEXT_PADDING
                )
                canvas.drawRoundRect(
                    backgroundRect,
                    20f, // Corner radius for X
                    20f, // Corner radius for Y
                    textBackgroundPaint
                )


                // Draw text for detected face (below the bounding box)
                canvas.drawText(
                    drawableText,
                    textLeft,
                    textTop,
                    textPaint
                )
            }
        }
    }


    fun setResults(
        faces: List<Faces>,
      //  detectionResults: FaceDetectorResult,
        imageHeight: Int,
        imageWidth: Int,
    ) {
        results = faces
        scaleFactor = min(width * 1f / imageWidth, height * 1f / imageHeight)
        invalidate()
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}