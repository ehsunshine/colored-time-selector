package de.ehsun.coloredtimebar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent

class TimelinePickerView @JvmOverloads constructor(context: Context,
                                                   attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0,
                                                   defStyleRes: Int = 0)
    : TimelineView(context, attrs, defStyleAttr, defStyleRes) {

    var pickerDrawable: Drawable = context.getDrawableCompat(R.drawable.ic_navigation_black_24dp)
    var stepSize: Int = 1
    var minSelectableTimeRange: Int = 15
    private var onSelectedTimeRangeChanged: ((from: SimpleTime, to: SimpleTime) -> Unit)? = null
    val selectedTimeRange: ClosedRange<SimpleTime>
        get() = SimpleTime.fromMinutes(handleLeftPos)..SimpleTime.fromMinutes(handleRightPos)

    private var handleLeftPos: Int by doOnChange(0) { postInvalidate() }
    private var handleRightPos: Int by doOnChange(60) { postInvalidate() }
    private lateinit var xToPosConverter: (Float) -> Int
    private var movingHandle: TimelineHandle? = null

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TimelinePickerView, 0, 0)
            pickerDrawable = typedArray.getDrawable(R.styleable.TimelinePickerView_pickerDrawable) ?: pickerDrawable
            stepSize = typedArray.getInt(R.styleable.TimelinePickerView_stepSize, stepSize)
            minSelectableTimeRange = stepSize
            typedArray.recycle()
        }
    }

    override fun setAvailableTimeRange(availableTimeRanges: List<String>) {
        super.setAvailableTimeRange(availableTimeRanges)
        availableRanges.firstOrNull { (start, end) -> end.toMinutes() - start.toMinutes() >= minSelectableTimeRange }
                ?.let { (start, _) ->
                    handleLeftPos = start.toMinutes()
                    handleRightPos = handleLeftPos + minSelectableTimeRange
                    highlightRange = SimpleTime.fromMinutes(handleLeftPos)..SimpleTime.fromMinutes(handleRightPos)
                }
    }

    fun setOnSelectedTimeRangeChangedListener(callback: (from: SimpleTime, to: SimpleTime) -> Unit) {
        this.onSelectedTimeRangeChanged = callback
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight + pickerDrawable.intrinsicHeight)

        xToPosConverter = { x ->
            val mainBarRange = timeRangeToRect.invoke(timeRange).run { left..right }
            when {
                x <= mainBarRange.start -> timeRange.start.toMinutes()
                x >= mainBarRange.endInclusive -> timeRange.endInclusive.toMinutes()
                else -> {
                    val k = (x - mainBarRange.start) / (mainBarRange.endInclusive - mainBarRange.start)
                    (timeRange.start.toMinutes() + k * (timeRange.endInclusive - timeRange.start).toMinutes()).toInt()
                }
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        canvas.drawHandles(handleLeftPos..handleRightPos)
    }

    private fun Canvas.drawHandles(range: IntRange) {
        val timeRange = SimpleTime.fromMinutes(range.start)..SimpleTime.fromMinutes(range.endInclusive)
        timeRangeToRect.invoke(timeRange)
                .let { rect ->
                    val handle1Left = (rect.left - (pickerDrawable.intrinsicWidth / 2f)).toInt()
                    val handle2Left = (rect.right - (pickerDrawable.intrinsicWidth / 2f)).toInt()
                    val drawableWidth = pickerDrawable.intrinsicWidth
                    val drawableHeight = pickerDrawable.intrinsicHeight
                    listOf(
                            Rect(handle1Left, rect.bottom.toInt(), handle1Left + drawableWidth, rect.bottom.toInt() + drawableHeight),
                            Rect(handle2Left, rect.bottom.toInt(), handle2Left + drawableWidth, rect.bottom.toInt() + drawableHeight)
                    )
                }
                .forEach {
                    pickerDrawable.bounds = it
                    pickerDrawable.draw(this)
                }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val pos = xToPosConverter(event.x)
                movingHandle = when {
                    Math.abs(handleLeftPos - pos) < Math.abs(handleRightPos - pos) -> TimelineHandle.LEFT
                    else -> TimelineHandle.RIGHT
                }
                when (movingHandle) {
                    TimelineHandle.LEFT -> setLeftHandle(pos)
                    TimelineHandle.RIGHT -> setRightHandle(pos)
                }
                true
            }
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
                val pos = xToPosConverter(event.x)
                when (movingHandle) {
                    TimelineHandle.LEFT -> setLeftHandle(pos)
                    TimelineHandle.RIGHT -> setRightHandle(pos)
                }
                true
            }
            MotionEvent.ACTION_UP -> {
                parent.requestDisallowInterceptTouchEvent(false)
                true
            }
            else -> false
        }
    }

    private fun setLeftHandle(newValue: Int) {
        var correctValue = roundPosByStep(newValue)
        if (handleRightPos - correctValue < minSelectableTimeRange) {
            correctValue = handleRightPos - minSelectableTimeRange
        }

        val leftAvailableRange = availableRanges.find { it.contains(correctValue) }
        if (leftAvailableRange != null) {
            if (!leftAvailableRange.contains(handleRightPos)) {
                if (leftAvailableRange.endInclusive.toMinutes() - correctValue >= minSelectableTimeRange) {
                    handleRightPos = leftAvailableRange.endInclusive.toMinutes()
                } else {
                    correctValue = availableRanges.find { it.contains(handleRightPos) }!!.start.toMinutes()
                }
            }
        } else {
            correctValue = availableRanges.find { it.contains(handleRightPos) }!!.start.toMinutes()
        }

        handleLeftPos = correctValue
        highlightRange = SimpleTime.fromMinutes(handleLeftPos)..SimpleTime.fromMinutes(handleRightPos)
        highlightRange?.let { onSelectedTimeRangeChanged?.invoke(it.start, it.endInclusive) }
    }

    private fun setRightHandle(newValue: Int) {
        var correctValue = roundPosByStep(newValue)
        if (correctValue - handleLeftPos < minSelectableTimeRange) {
            correctValue = handleLeftPos + minSelectableTimeRange
        }

        val rightAvailableRange = availableRanges.find { it.contains(correctValue) }
        if (rightAvailableRange != null) {
            if (!rightAvailableRange.contains(handleLeftPos)) {
                if (correctValue - rightAvailableRange.start.toMinutes() >= minSelectableTimeRange) {
                    handleLeftPos = rightAvailableRange.start.toMinutes()
                } else {
                    correctValue = availableRanges.find { it.contains(handleLeftPos) }!!.endInclusive.toMinutes()
                }
            }
        } else {
            correctValue = availableRanges.find { it.contains(handleLeftPos) }!!.endInclusive.toMinutes()
        }

        handleRightPos = correctValue
        highlightRange = SimpleTime.fromMinutes(handleLeftPos)..SimpleTime.fromMinutes(handleRightPos)
        highlightRange?.let { onSelectedTimeRangeChanged?.invoke(it.start, it.endInclusive) }
    }

    private fun roundPosByStep(pos: Int) = (pos / stepSize) * stepSize

    private fun ClosedRange<SimpleTime>.contains(value: Int) = start.toMinutes() <= value && value <= endInclusive.toMinutes()

    private enum class TimelineHandle { LEFT, RIGHT }
}