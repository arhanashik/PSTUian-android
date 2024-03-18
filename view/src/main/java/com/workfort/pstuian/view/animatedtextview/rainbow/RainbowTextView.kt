package com.workfort.pstuian.view.animatedtextview.rainbow

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.util.AttributeSet
import com.workfort.pstuian.view.R
import com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView
import com.workfort.pstuian.view.animatedtextview.base.AnimationListener
import com.workfort.pstuian.view.animatedtextview.base.DisplayUtils.dp2px

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 1:10.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class RainbowTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AnimatedTextView(context, attrs, defStyleAttr) {
    private var mMatrix: Matrix = Matrix()
    private var mTranslate = 0f
    private var colorSpeed = 0f
    private var colorSpace = 0f
    private var colors =
        intArrayOf(-0xd4de, -0x80de, -0x1200de, -0xdd00de, -0xdd0b01, -0xddc601, -0xabff09)
    private var mLinearGradient: LinearGradient? = null

    init {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.RainbowTextView)
        colorSpace = typedArray.getDimension(
            R.styleable.RainbowTextView_colorSpace,
            dp2px(150f).toFloat()
        )
        colorSpeed = typedArray.getDimension(
            R.styleable.RainbowTextView_colorSpeed,
            dp2px(5f).toFloat()
        )
        typedArray.recycle()
        mMatrix = Matrix()
        initPaint()
    }
    override fun setAnimationListener(listener: AnimationListener) {
        throw UnsupportedOperationException("Invalid operation for rainbow")
    }

    fun getColorSpace(): Float = colorSpace

    fun setColorSpace(colorSpace: Float) {
        this.colorSpace = colorSpace
    }

    fun getColorSpeed(): Float = colorSpeed

    fun setColorSpeed(colorSpeed: Float) {
        this.colorSpeed = colorSpeed
    }

    fun setColors(vararg colors: Int) {
        this.colors = colors
        initPaint()
    }

    private fun initPaint() {
        mLinearGradient = LinearGradient(0f, 0f, colorSpace, 0f, colors,
            null, Shader.TileMode.MIRROR)
        paint.shader = mLinearGradient
    }

    override fun setProgress(progress: Float) {}
    override fun animateText(text: CharSequence) {
        setText(text)
    }

    override fun onDraw(canvas: Canvas) {
        mTranslate += colorSpeed
        mMatrix.setTranslate(mTranslate, 0f)
        mLinearGradient!!.setLocalMatrix(mMatrix)
        super.onDraw(canvas)
        postInvalidateDelayed(100)
    }
}