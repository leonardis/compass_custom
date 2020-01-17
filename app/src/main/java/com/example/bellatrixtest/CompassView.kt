package com.example.bellatrixtest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.max


class CompassView constructor(
    context: Context,
    attributeSet: AttributeSet? = null
): ConstraintLayout(context, attributeSet) {

    private var showGrades = true
    private var compassColor = Color.BLACK
    private var position = 0f
    private var size = 300

    init {
        setupAttributes(attributeSet)
    }

    private fun setupAttributes(attributeSet: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.CompassView)

        showGrades = attributes.getBoolean(R.styleable.CompassView_showGrades, showGrades)
        compassColor = attributes.getColor(R.styleable.CompassView_compassColor, compassColor)

        attributes.recycle()
    }

    override fun dispatchDraw(canvas: Canvas) {
        drawCompass(canvas)
        super.dispatchDraw(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        drawCompass(canvas)
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defaultSize = resources.getDimensionPixelSize(R.dimen.compass_default_size)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = if (widthMode == MeasureSpec.EXACTLY && widthSize <= defaultSize) widthSize else defaultSize
        val height = if (heightMode == MeasureSpec.EXACTLY && heightSize <= defaultSize) heightSize else defaultSize

        size = if (width > height) height else width

        setMeasuredDimension(size, size)
    }

    private fun drawCompass(canvas: Canvas) {
        val height = this.height
        val width = this.width

        val xPoint = measuredWidth / 2f
        val yPoint = measuredHeight / 2f

        if (showGrades) {
            drawGradesByPosition(canvas, xPoint)
        }

        canvas.rotate(-position, width / 2f, height / 2f)

        drawPointerArrow(canvas, xPoint)
        drawGrades(canvas, xPoint, yPoint)
        drawCardinalPoints(canvas, xPoint, yPoint)
        drawCross(canvas, xPoint, yPoint)
        drawDashedCompassCircle(canvas, xPoint, yPoint)
        invalidate()
    }

    fun updatePosition(position: Float) {
        this.position = position
        this.invalidate()
    }

    fun changeColor(color: Int) {
        compassColor = color
    }

    private fun drawPointerArrow(canvas: Canvas, centerX: Float) {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.RED

        val y = getDimenBasedOnMeasure(125f)
        val arrowBaseSize = getDimenBasedOnMeasure(10f)

        val path = Path()
        path.moveTo(centerX, y - arrowBaseSize)
        path.lineTo(centerX - arrowBaseSize, y + arrowBaseSize)
        path.lineTo(centerX + arrowBaseSize, y + arrowBaseSize)
        path.lineTo(centerX, y - arrowBaseSize)
        path.close()

        canvas.drawPath(path, paint)
    }
    
    private fun drawGradesByPosition(canvas: Canvas, centerX: Float) {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.textSize = getDimenBasedOnMeasure(70f)
        paint.color = compassColor

        drawTextWithPaint(canvas, paint, getGradesBy(position.toInt()), 0f, 0f, TextCenterAlignment.BOTTOM)
    }
    
    private fun drawGrades(canvas: Canvas, centerX: Float, centerY: Float) {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.textSize = getDimenBasedOnMeasure(30f)
        paint.color = compassColor

        drawTextWithPaint(canvas, paint, "0", centerX, 100f, TextCenterAlignment.CENTER_TOP)
        drawTextWithPaint(canvas, paint, "90", centerY, 760f, TextCenterAlignment.CENTER_END)
        drawTextWithPaint(canvas, paint, "180", centerX, 100f, TextCenterAlignment.CENTER_BOTTOM)
        drawTextWithPaint(canvas, paint, "270", centerY, 80f, TextCenterAlignment.CENTER_START)
    }
    
    private fun drawCardinalPoints(canvas: Canvas, centerX: Float, centerY: Float) {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.textSize = getDimenBasedOnMeasure(50f)
        paint.color = compassColor

        drawTextWithPaint(canvas, paint, "N", centerX, 280f, TextCenterAlignment.CENTER_TOP)
        drawTextWithPaint(canvas, paint, "E", centerY, 630f, TextCenterAlignment.CENTER_END)
        drawTextWithPaint(canvas, paint, "S", centerX, 250f, TextCenterAlignment.CENTER_BOTTOM)
        drawTextWithPaint(canvas, paint, "W", centerY, 250f, TextCenterAlignment.CENTER_START)
    }

    private fun drawTextWithPaint(canvas: Canvas, paint: Paint, text: String, centerFactor: Float, baseSize: Float, alignment: TextCenterAlignment) {
        val middleBound = paint.measureText(text) / 2
        val xAlignment = when (alignment) {
            TextCenterAlignment.CENTER_TOP, TextCenterAlignment.CENTER_BOTTOM -> centerFactor - middleBound
            TextCenterAlignment.CENTER_END, TextCenterAlignment.END -> getDimenBasedOnMeasure(baseSize)
            TextCenterAlignment.START, TextCenterAlignment.CENTER_START -> getDimenBasedOnMeasure(baseSize)
            else -> 0f
        }

        val yAlignment = when (alignment) {
            TextCenterAlignment.TOP, TextCenterAlignment.CENTER_TOP -> getDimenBasedOnMeasure(baseSize)
            TextCenterAlignment.CENTER_END -> centerFactor + middleBound
            TextCenterAlignment.BOTTOM, TextCenterAlignment.CENTER_BOTTOM -> height - getDimenBasedOnMeasure(baseSize)
            TextCenterAlignment.CENTER_START -> centerFactor + middleBound
            else -> 0f
        }

        canvas.drawText(
            text,
            xAlignment,
            yAlignment,
            paint
        )
    }
    
    private fun drawCross(canvas: Canvas, centerX: Float, centerY: Float) {
        val paint = Paint()
        paint.color = compassColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        val measuredDimen = getDimenBasedOnMeasure(50f)
        canvas.drawLine(centerX - measuredDimen, centerY, centerX + measuredDimen, centerY, paint)
        canvas.drawLine(centerX, centerY - measuredDimen, centerX, centerY + measuredDimen, paint)
    }

    private fun drawDashedCompassCircle(canvas: Canvas, centerX: Float, centerY: Float) {
        val dashPath = DashPathEffect(floatArrayOf(7f, 7f), 1.0.toFloat())
        val radius = (max(centerX, centerY) * 0.6).toFloat()

        val paint = Paint()
        paint.color = compassColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = getDimenBasedOnMeasure(50f)
        paint.pathEffect = dashPath

        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    private fun getGradesBy(position: Int): String {
        return when (position) {
            in 0..30 -> resources.getString(R.string.north_initial, position)
            in 30..60 -> resources.getString(R.string.north_east_initial, position)
            in 60..120 -> resources.getString(R.string.east_initial, position)
            in 120..150 -> resources.getString(R.string.south_east_initial, position)
            in 150..210 -> resources.getString(R.string.south_initial, position)
            in 210..240 -> resources.getString(R.string.south_west_initial, position)
            in 240..300 -> resources.getString(R.string.west_initial, position)
            in 300..330 -> resources.getString(R.string.north_west_initial, position)
            else -> resources.getString(R.string.north_east_initial, position)
        }
    }

    private fun getDimenBasedOnMeasure(baseSize: Float): Float {
        return (baseSize * size) / 900
    }
}

enum class TextCenterAlignment {
    TOP, END, BOTTOM, START, CENTER_TOP, CENTER_END, CENTER_BOTTOM, CENTER_START
}