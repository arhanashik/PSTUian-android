package com.workfort.pstuian.view.animatedtextview.base

import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.view.ViewCompat

/**
 *  ****************************************************************************
 *  * Created by : arhan on 26 Nov, 2021 at 23:41.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

abstract class AnimatedText : com.workfort.pstuian.view.animatedtextview.base.IAnimatedText {
    lateinit var mAnimatedTextView: com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView
    lateinit var mText: CharSequence
    var mOldText: CharSequence = ""
    var mPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    var mOldPaint: TextPaint = TextPaint(mPaint)
    private var mHeight = 0
    private var mWidth = 0
    var gapList: MutableList<Float> = ArrayList()
    var oldGapList: MutableList<Float> = ArrayList()
    private var progress = 0f // 0 ~ 1
    open fun getProgress() = progress
    var mTextSize = 0f
    var oldStartX = 0f
    private var animationListener: com.workfort.pstuian.view.animatedtextview.base.AnimationListener? = null
    open fun getAnimationListener(): com.workfort.pstuian.view.animatedtextview.base.AnimationListener? {
        return animationListener
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        mAnimatedTextView.invalidate()
    }

    override fun init(animatedTextView: com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView, attrs: AttributeSet?, defStyle: Int) {
        mAnimatedTextView = animatedTextView
        mText = animatedTextView.text
        mOldText = ""
        mPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mOldPaint = TextPaint(mPaint)
        progress = 1f
        mAnimatedTextView.apply {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    mTextSize = textSize
                    mWidth = width
                    mHeight = height
                    oldStartX = 0f
                    try {
                        val layoutDirection = ViewCompat.getLayoutDirection(this@apply)
                        oldStartX = if (layoutDirection == ViewCompat.LAYOUT_DIRECTION_LTR)
                                layout.getLineLeft(0) else layout.getLineRight(0)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    initVariables()
                }
            })
        }
        prepareAnimate()
    }

    private fun prepareAnimate() {
        mTextSize = mAnimatedTextView.textSize
        mPaint.apply {
            textSize = mTextSize
            color = mAnimatedTextView.currentTextColor
            typeface = mAnimatedTextView.typeface
            gapList.clear()
            for (char in mText) {
                gapList.add(measureText(char.toString()))
            }
        }
        mOldPaint.apply {
            textSize = mTextSize
            color = mAnimatedTextView.currentTextColor
            typeface = mAnimatedTextView.typeface
            oldGapList.clear()
            for (char in mOldText) {
                oldGapList.add(measureText(char.toString()))
            }
        }
    }

    override fun animateText(text: CharSequence) {
        mAnimatedTextView.text = text
        mOldText = mText
        mText = text
        prepareAnimate()
        animatePrepare(text)
        animateStart(text)
    }

    override fun setAnimationListener(listener: com.workfort.pstuian.view.animatedtextview.base.AnimationListener) {
        animationListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        drawFrame(canvas)
    }

    protected abstract fun initVariables()
    protected abstract fun animateStart(text: CharSequence?)
    protected abstract fun animatePrepare(text: CharSequence?)
    protected abstract fun drawFrame(canvas: Canvas?)
}