package com.example.task_2

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.math.cos
import kotlin.math.sin

class CircularProgressBar(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    private var progress = 0F

    private var rectF = RectF()

    private var trackLineGap = 4
    private var trackWidth = 50
    private var trackLineThickness = 10

    private var size = 300
    private var startAngle = 0
    private var arcFullRotationAngle = 360
    private var percentageDivider = 10

    private val baseCirclePaint by lazy {
        Paint().apply {
            color = Color.GRAY
            strokeWidth = trackLineThickness.toFloat()
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
    }

    private val fillProgressPaint by lazy {
        Paint().apply {
            color = Color.parseColor("#00C853")
            strokeWidth = trackWidth.toFloat()
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
    }

    init {
        val attributes = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.CircularProgressBar,
            0,
            0
        )

        size = attributes.getInteger(R.styleable.CircularProgressBar_size, size)
        startAngle = attributes.getInteger(R.styleable.CircularProgressBar_startAngle, startAngle)
        arcFullRotationAngle = attributes.getInteger(
            R.styleable.CircularProgressBar_arcFullRotationAngle,
            arcFullRotationAngle
        )
        percentageDivider = attributes.getInteger(
            R.styleable.CircularProgressBar_percentageDivider,
            percentageDivider
        )

        trackLineGap =
            attributes.getInteger(R.styleable.CircularProgressBar_trackLineGap, trackLineGap)
        trackWidth = attributes.getInteger(R.styleable.CircularProgressBar_trackWidth, trackWidth)
        trackLineThickness = attributes.getInteger(
            R.styleable.CircularProgressBar_trackLineThickness,
            trackLineThickness
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.let {
            createRect()
            drawBaseCircle(it)
            drawFillArc(it)
        }
    }

    private fun createRect() {
        val cx = width / 2f
        val cy = height / 2f

        rectF.set(
            cx - size,
            cy - size,
            cx + size,
            cy + size
        )
    }

    private fun drawBaseCircle(canvas: Canvas) {
        canvas.drawPath(getDashPatternPath(), baseCirclePaint)
    }

    private fun drawFillArc(canvas: Canvas) {
        val left = rectF.left + trackWidth / 2
        val right = rectF.right - trackWidth / 2
        val top = rectF.top + trackWidth / 2
        val bottom = rectF.bottom - trackWidth / 2

        canvas.drawArc(
            left,
            top,
            right,
            bottom,
            startAngle.toFloat(),
            getCurrentPercentageToFill(),
            false,
            fillProgressPaint
        )
    }

    private fun getCurrentPercentageToFill() =
        (arcFullRotationAngle * (progress / percentageDivider))

    private fun getDashPatternPath(): Path {
        val cx = rectF.centerX()
        val cy = rectF.centerY()

        val radiusOuterCircle = rectF.width() / 2
        val radiusInnerCircle = radiusOuterCircle - trackWidth

        val path = Path().apply {
            var angle = 0F
            while (angle <= arcFullRotationAngle) {
                Math.toRadians(angle.toDouble()).apply {
                    val x1 = cx + radiusOuterCircle * cos(this)
                    val y1 = cy + radiusOuterCircle * sin(this)
                    val x2 = cx + radiusInnerCircle * cos(this)
                    val y2 = cy + radiusInnerCircle * sin(this)

                    moveTo(x1.toFloat(), y1.toFloat())
                    lineTo(x2.toFloat(), y2.toFloat())
                }
                angle += trackLineGap
            }
        }
        return path
    }

    fun animateProgress(percent: Int) {
        val valuesHolder =
            PropertyValuesHolder.ofInt(PERCENTAGE_VALUE_HOLDER, progress.toInt(), percent)
        val animator = ValueAnimator().apply {
            setValues(valuesHolder)
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener {
                val percentage = it.getAnimatedValue(PERCENTAGE_VALUE_HOLDER) as Int
                progress = percentage.toFloat()
                invalidate()
            }
        }
        animator.start()
    }

    companion object {
        const val PERCENTAGE_VALUE_HOLDER = "percentage"
    }

}