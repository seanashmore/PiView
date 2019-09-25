package com.alittlelost.piview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

class PiView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    companion object {
        private val TAG = this::class.java.simpleName

        const val SEGMENT_SCALED = 0.2f
        const val SEGMENT_REGULAR = 1.0f
        const val PADDING = 50.0f
    }

    private val circlePaint = Paint()
    private val segmentPaint = Paint()
    private val segmentStrokePaint = Paint()
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val debugPaint = Paint()

    private var circleCenterX: Float = 0.0f
    private var circleCenterY: Float = 0.0f
    private var circleRadius: Float = 0.0f
    private var currentDegree = 0.0f

    private val segmentManager = SegmentManager()

    private var scale = 1.0f

    private var strokeSegments: Boolean = false

    private var oval = RectF()

    private var scaleAnimator: ValueAnimator
    private var deScaleAnimator: ValueAnimator

    private lateinit var onSegmentClickListener: OnSegmentClickListener

    init {
        segmentPaint.color = ContextCompat.getColor(context, android.R.color.holo_red_dark)

        segmentStrokePaint.color = ContextCompat.getColor(context, android.R.color.black)
        segmentStrokePaint.style = Paint.Style.STROKE
        segmentStrokePaint.strokeWidth = 10.0f

        circlePaint.color = ContextCompat.getColor(context, android.R.color.black)
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 10.0f

        textPaint.color = ContextCompat.getColor(context, android.R.color.holo_red_dark)
        textPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.textSize = 40.0f

        debugPaint.color = ContextCompat.getColor(context, android.R.color.holo_orange_light)
        debugPaint.strokeWidth = 5.0f
        debugPaint.style = Paint.Style.STROKE

        val attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.PiView, 0, 0)
        strokeSegments = attrArray.getBoolean(R.styleable.PiView_strokeSegments, false)
        attrArray.recycle()

        scaleAnimator = ValueAnimator.ofFloat(SEGMENT_REGULAR, SEGMENT_SCALED)
        deScaleAnimator = ValueAnimator.ofFloat(SEGMENT_SCALED, SEGMENT_REGULAR)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (isInsideCircle(event)) {
                    for (segment in segmentManager.segments) {
                        if (isInsideSegment(event, segment)) {
                            if(::onSegmentClickListener.isInitialized) {
                                onSegmentClickListener.onSegmentTapped(segment)
                            }
                            if (segment.scale == SEGMENT_SCALED) {
                                segment.startDescaleAnimator().addUpdateListener { invalidate() }
                            } else if (segment.scale == SEGMENT_REGULAR) {
                                segment.startScaleAnimator().addUpdateListener { invalidate() }
                            }
                        } else {
                            if (segment.scale == SEGMENT_SCALED) {
                                segment.startDescaleAnimator().addUpdateListener { invalidate() }
                            }
                        }
                    }
                }

            }
        }
        return super.onTouchEvent(event)
    }

    private fun isInsideCircle(event: MotionEvent): Boolean {
        val xSq = (event.x - circleCenterX) * (event.x - circleCenterX)
        val ySq = (event.y - circleCenterY) * (event.y - circleCenterY)
        val sum = xSq + ySq
        val distance = sqrt(sum.toDouble())
        return distance <= circleRadius
    }

    private fun isInsideSegment(event: MotionEvent, segment: Segment): Boolean {
        var degrees =
            Math.toDegrees(atan2(event.y - circleCenterY, event.x - circleCenterX).toDouble())

        if (degrees < 0) {
            degrees = (360.0 - abs(degrees))
        }

        return degrees >= segment.startAngle && degrees <= segment.endAngle
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) {
            return
        }

        for (segment in segmentManager.segments) {

            scale = if (segment.percentage < 50) {
                1.0f
            } else {
                1.0f
            }

            circleCenterX = (width / 2).toFloat()
            circleCenterY = (height / 2).toFloat()

            oval = RectF(
                0.0f + PADDING * segment.scale, 0.0f + PADDING * segment.scale,
                width.toFloat() - PADDING * segment.scale,
                height.toFloat() - PADDING * segment.scale
            )

            circleRadius = oval.width() / 2 - PADDING

            segmentPaint.color = ContextCompat.getColor(context, segment.colorResId)

            drawSegment(canvas, segment, segmentPaint)

            if (strokeSegments) {
                drawSegment(canvas, segment, segmentStrokePaint)
            }

            currentDegree += (360.0f / 100) * segment.percentage
        }
    }

    private fun drawSegment(canvas: Canvas, segment: Segment, paint: Paint) {
        canvas.drawArc(
            oval,
            currentDegree,
            ((360.0f / 100) * segment.percentage), true,
            paint
        )
    }

    fun addSegment(segment: Segment) {
        segmentManager.addSegment(segment)
    }

    fun removeSegment(segment: Segment) {
        segmentManager.removeSegment(segment)
    }

    fun setOnSegmentClickListener(onSegmentClickListener: OnSegmentClickListener) {
        this.onSegmentClickListener = onSegmentClickListener
    }

    interface OnSegmentClickListener {
        fun onSegmentTapped(segment: Segment)
    }
}