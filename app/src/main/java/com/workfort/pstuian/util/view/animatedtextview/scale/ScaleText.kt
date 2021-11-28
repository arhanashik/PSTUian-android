package com.workfort.pstuian.util.view.animatedtextview.scale

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import com.workfort.pstuian.util.view.animatedtextview.base.*
import java.util.*
import kotlin.math.max

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 1:13.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/11/27.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class ScaleText : AnimatedText() {
    private var mostCount = 20f
    private var charTime = 400f
    private val differentList: MutableList<CharacterDiffResult> = ArrayList()
    private var duration: Long = 0
    private var animator: ValueAnimator = ValueAnimator()

    override fun init(animatedTextView: AnimatedTextView, attrs: AttributeSet?, defStyle: Int) {
        super.init(animatedTextView, attrs, defStyle)
        animator = ValueAnimator()
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addListener(object : DefaultAnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                getAnimationListener()?.onAnimationEnd(mAnimatedTextView)
            }
        })
        animator.addUpdateListener { animation ->
            setProgress(animation.animatedValue as Float)
        }
        var n: Int = mText.length
        n = if (n <= 0) 1 else n
        duration = (charTime + charTime / mostCount * (n - 1)).toLong()
    }

    override fun animateText(text: CharSequence) {
        mAnimatedTextView.post(Runnable {
            if (mAnimatedTextView.layout == null) {
                return@Runnable
            }
            oldStartX = mAnimatedTextView.layout.getLineLeft(0)
            super@ScaleText.animateText(text)
        })
    }

    override fun initVariables() {}
    override fun animatePrepare(text: CharSequence?) {
        differentList.clear()
        differentList.addAll(CharacterUtils.diff(mOldText, mText))
    }

    override fun animateStart(text: CharSequence?) {
        var n: Int = mText.length
        n = if (n <= 0) 1 else n
        duration = (charTime + charTime / mostCount * (n - 1)).toLong()
        animator.apply {
            cancel()
            setFloatValues(0f, 1f)
            duration = duration
            start()
        }
    }

    override fun drawFrame(canvas: Canvas?) {
        val startX: Float = mAnimatedTextView.layout.getLineLeft(0)
        val startY: Float = mAnimatedTextView.baseline.toFloat()
        var offset = startX
        var oldOffset: Float = oldStartX
        val maxLength: Int = max(mText.length, mOldText.length)
        for (i in 0 until maxLength) {
            // draw old text
            if (i < mOldText.length) {
                val move: Int = CharacterUtils.needMove(i, differentList)
                if (move != -1) {
                    mOldPaint.textSize = mTextSize
                    mOldPaint.alpha = 255
                    var p: Float = getProgress() * 2f
                    p = if (p > 1) 1f else p
                    val distX: Float =
                        CharacterUtils.getOffset(i, move, p, startX, oldStartX, gapList, oldGapList)
                    canvas?.drawText(
                        mOldText[i].toString() + "",
                        0,
                        1,
                        distX,
                        startY,
                        mOldPaint
                    )
                } else {
                    mOldPaint.alpha = ((1 - getProgress()) * 255).toInt()
                    mOldPaint.textSize = mTextSize * (1 - getProgress())
                    val width: Float = mOldPaint.measureText(mOldText[i].toString() + "")
                    canvas?.drawText(
                        mOldText[i].toString() + "",
                        0,
                        1,
                        oldOffset + (oldGapList[i] - width) / 2,
                        startY,
                        mOldPaint
                    )
                }
                oldOffset += oldGapList[i]
            }

            // draw new text
            if (i < mText.length) {
                if (!CharacterUtils.stayHere(i, differentList)) {
                    var alpha =
                        (255f / charTime * (getProgress() * duration - charTime * i / mostCount)).toInt()
                    if (alpha > 255) alpha = 255
                    if (alpha < 0) alpha = 0
                    var size: Float =
                        mTextSize * 1f / charTime * (getProgress() * duration - charTime * i / mostCount)
                    if (size > mTextSize) size = mTextSize
                    if (size < 0) size = 0f
                    mPaint.alpha = alpha
                    mPaint.textSize = size
                    val width: Float = mPaint.measureText(mText[i].toString() + "")
                    canvas?.drawText(
                        mText[i].toString() + "",
                        0,
                        1,
                        offset + (gapList[i] - width) / 2,
                        startY,
                        mPaint
                    )
                }
                offset += gapList[i]
            }
        }
    }
}