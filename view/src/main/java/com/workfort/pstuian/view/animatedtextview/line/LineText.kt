package com.workfort.pstuian.view.animatedtextview.line

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.workfort.pstuian.view.R
import com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView
import com.workfort.pstuian.view.animatedtextview.base.DefaultAnimatorListener
import com.workfort.pstuian.view.animatedtextview.base.DisplayUtils.dp2px
import kotlin.math.sqrt

class LineText : com.workfort.pstuian.view.animatedtextview.base.AnimatedText() {
    private var p1 = PointF()
    private var p2 = PointF()
    private var p3 = PointF()
    private var p4 = PointF()
    private var p5 = PointF()
    private var p6 = PointF()
    private var p7 = PointF()
    private var p8 = PointF()
    private var pA = PointF()
    private var pB = PointF()
    private var pC = PointF()
    private var pD = PointF()
    private var animationDuration = 0f
    private var lineWidth = 0f
    private var lineColor = 0
    private var mLinePaint: Paint? = null

    fun setLineColor(color: Int) {
        lineColor = color
        mLinePaint?.color = color
    }

    fun getLineWidth() = lineWidth

    fun setLineWidth(lineWidth: Float) {
        this.lineWidth = lineWidth
    }

    fun getAnimationDuration() = animationDuration

    fun setAnimationDuration(animationDuration: Float) {
        this.animationDuration = animationDuration
    }

    override fun init(animatedTextView: AnimatedTextView, attrs: AttributeSet?, defStyle: Int) {
        super.init(animatedTextView, attrs, defStyle)
        val typedArray: TypedArray =
            animatedTextView.context.obtainStyledAttributes(attrs, R.styleable.LineTextView)
        animationDuration =
            typedArray.getInt(R.styleable.LineTextView_lineAnimationDuration, DEFAULT_DURATION)
                .toFloat()
        lineColor =
            typedArray.getColor(R.styleable.LineTextView_lineColor, animatedTextView.currentTextColor)
        lineWidth = typedArray.getDimension(
            R.styleable.LineTextView_lineWidth,
            DEFAULT_LINE_WIDTH.toFloat()
        )
        typedArray.recycle()
    }

    override fun initVariables() {
        lineWidth = DEFAULT_LINE_WIDTH.toFloat()
        animationDuration = DEFAULT_DURATION.toFloat()
        mLinePaint = Paint().apply {
            color = lineColor
            strokeWidth = lineWidth
        }
    }

    override fun animateStart(text: CharSequence?) {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            .setDuration(animationDuration.toLong())
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addListener(object : DefaultAnimatorListener() {
            override fun onAnimationEnd(animation: Animator) {
                getAnimationListener()?.onAnimationEnd(mAnimatedTextView)
            }
        })
        valueAnimator.addUpdateListener { animation ->
            setProgress(animation.animatedValue as Float)
        }
        valueAnimator.start()
    }

    override fun animatePrepare(text: CharSequence?) {}

    override fun drawFrame(canvas: Canvas?) {
        val percent = getProgress()
        val percent2 = (sqrt((3.38f - (percent - 1.7f) * (percent - 1.7f)).toDouble()) - 0.7)
            .toFloat()
        if(canvas == null) return
        val width: Int = mAnimatedTextView.width
        val height: Int = mAnimatedTextView.height
        pA.x = 0f
        pA.y = 0f
        pB.x = width.toFloat()
        pB.y = 0f
        pC.x = width.toFloat()
        pC.y = height.toFloat()
        pD.x = 0f
        pD.y = height.toFloat()
        p1.x = width * percent2
        p1.y = pB.y
        drawLine(canvas, p1, pB)
        p2.x = pB.x
        p2.y = height * percent
        drawLine(canvas, pB, p2)
        p3.x = width.toFloat()
        p3.y = height * percent2
        drawLine(canvas, p3, pC)
        p4.x = width * (1 - percent)
        p4.y = height.toFloat()
        drawLine(canvas, pC, p4)
        p5.x = width * (1 - percent2)
        p5.y = height.toFloat()
        drawLine(canvas, p5, pD)
        p6.x = 0f
        p6.y = height * (1 - percent)
        drawLine(canvas, pD, p6)
        p7.x = 0f
        p7.y = height * (1 - percent2)
        drawLine(canvas, p7, pA)
        p8.x = width * percent
        p8.y = 0f
        drawLine(canvas, pA, p8)
    }

    private fun drawLine(canvas: Canvas, a: PointF, b: PointF) {
        mLinePaint?.let { canvas.drawLine(a.x, a.y, b.x, b.y, it) }
    }

    companion object {
        const val DEFAULT_DURATION = 800
        val DEFAULT_LINE_WIDTH = dp2px(3f)
    }
}