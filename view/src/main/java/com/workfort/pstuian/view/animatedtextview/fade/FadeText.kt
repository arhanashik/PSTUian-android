package com.workfort.pstuian.view.animatedtextview.fade

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.res.TypedArray
import android.graphics.Canvas
import android.text.Layout
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.workfort.pstuian.view.R
import com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView
import com.workfort.pstuian.view.animatedtextview.base.DefaultAnimatorListener
import java.util.Random

class FadeText : com.workfort.pstuian.view.animatedtextview.base.AnimatedText() {
    private var random = Random()
    private var animationDuration = 0
    private var alphaList: MutableList<Int> = ArrayList()
    companion object {
       private const val DEFAULT_DURATION = 2000
    }

    override fun init(animatedTextView: AnimatedTextView, attrs: AttributeSet?, defStyle: Int) {
        super.init(animatedTextView, attrs, defStyle)
        val typedArray: TypedArray =
            animatedTextView.context.obtainStyledAttributes(attrs, R.styleable.FadeTextView)
        animationDuration =
            typedArray.getInt(R.styleable.FadeTextView_fadeAnimationDuration, DEFAULT_DURATION)
        typedArray.recycle()
    }

    fun setAnimationDuration(animationDuration: Int) {
        this.animationDuration = animationDuration
    }

    fun getAnimationDuration(): Int = animationDuration

    override fun initVariables() {
        // generate random alpha
        alphaList.clear()
        mAnimatedTextView.length().let { length ->
            for (i in 0 until length) {
                val randomNumber = random.nextInt(2) // 0 or 1
                val alpha = if ((i + 1) % (randomNumber + 2) == 0) { // 2 or 3
                    if ((i + 1) % (randomNumber + 4) == 0) { // 4 or 5
                        55
                    } else 255
                } else {
                    if ((i + 1) % (randomNumber + 4) == 0) { // 4 or 5
                        55
                    } else 0
                }
                alphaList.add(alpha)
            }
        }
    }

    override fun animateStart(text: CharSequence?) {
        initVariables()
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
        val layout: Layout = mAnimatedTextView.layout
        var gapIndex = 0
        for (i in 0 until layout.lineCount) {
            val lineStart = layout.getLineStart(i)
            val lineEnd = layout.getLineEnd(i)
            var lineLeft = layout.getLineLeft(i)
            val lineBaseline = layout.getLineBaseline(i).toFloat()
            val lineText: String = mText.subSequence(lineStart, lineEnd).toString()
            for (element in lineText) {
                val alpha = alphaList[gapIndex]
                mPaint.alpha = ((255 - alpha) * getProgress() + alpha).toInt()
                canvas?.drawText(element.toString(), lineLeft, lineBaseline, mPaint)
                lineLeft += gapList[gapIndex++]
            }
        }
    }
}