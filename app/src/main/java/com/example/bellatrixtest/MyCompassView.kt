package com.example.bellatrixtest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ImageView

class MyCompassView (context: Context, attributeSet: AttributeSet? = null): ImageView(context, attributeSet) {
    private var paint: Paint? = null
    private var position = 0f

    init {
        paint = Paint()

        paint?.isAntiAlias = true
        paint?.strokeWidth = 50f
        paint?.textSize = 105f
        paint?.style = Paint.Style.STROKE
        paint?.color = Color.BLUE

        //this.setImageResource(R.drawable.compass)
        /*paint?.isAntiAlias = true
        paint?.strokeWidth = 2f
        paint?.textSize = 25f
        paint?.style = Paint.Style.STROKE
        paint?.color = Color.RED*/
    }

    override fun onDraw(canvas: Canvas) {
        val height = this.height
        val width = this.width

        val xPoint = measuredWidth / 2f
        val yPoint = measuredHeight / 2f

        val radius = (Math.max(xPoint, yPoint) * 0.6).toFloat()

        canvas.rotate(-position, width / 2f, height / 2f)
        paint?.let {
            val dashPath = DashPathEffect(floatArrayOf(7f, 7f), 1.0.toFloat())

            it.setPathEffect(dashPath)
            canvas.drawCircle(xPoint, yPoint, radius, it)

            invalidate()
            /*canvas.drawCircle(xPoint, yPoint, radius, it)
            canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), it)

            // 3.143 is a good approximation for the circle
            canvas.drawLine(
                xPoint,
                yPoint,
                (xPoint + radius * Math.sin((-position).toDouble() / 180 * 3.143)).toFloat(),
                (yPoint - radius * Math.cos((-position).toDouble() / 180 * 3.143)).toFloat(), it
            )


            canvas.drawText(getGradesAndPosition(position.toInt()), xPoint, height.toFloat(), it)*/
        }
        super.onDraw(canvas)
    }

    fun updatePosition(position: Float) {
        this.position = position
        this.invalidate()
    }

    private fun getGradesAndPosition(position: Int): String {
        return when (position) {
            in 0..30 -> "$position° N"
            in 30..60 -> "$position° NE"
            in 60..120 -> "$position° E"
            in 120..150 -> "$position° SE"
            in 150..210 -> "$position° S"
            in 210..240 -> "$position° SW"
            in 240..300 -> "$position° W"
            in 300..330 -> "$position° NW"
            else -> "$position° N"
        }
    }
        /*val xPoint = measuredWidth / 2f
        val yPoint = measuredHeight / 2f

        val radius = (Math.max(xPoint, yPoint) * 0.6).toFloat()

        paint?.let {
            canvas.drawCircle(xPoint, yPoint, radius, it)
            canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), it)

            // 3.143 is a good approximation for the circle
            canvas.drawLine(
                xPoint,
                yPoint,
                (xPoint + radius * Math.sin((-position).toDouble() / 180 * 3.143)).toFloat(),
                (yPoint - radius * Math.cos((-position).toDouble() / 180 * 3.143)).toFloat(), it
            )

            canvas.drawText(position.toString(), xPoint, yPoint, it)
        }
    }

    fun updateData(position: Float) {
        this.position = position        invalidate()
    }*/
}