package com.workfort.pstuian.util.view.animatedtextview.evaporate

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import com.workfort.pstuian.util.view.animatedtextview.base.*
import java.util.*
import kotlin.math.max

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 0:19.
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

class EvaporateText : AnimatedText() {
    private var charTime = 300f
    private var mostCount = 20
    private var mTextHeight = 0
    private val differentList: MutableList<CharacterDiffResult> = ArrayList()
    private var duration: Long = 0
    private var animator: ValueAnimator = ValueAnimator()
    override fun init(animatedTextView: AnimatedTextView, attrs: AttributeSet?, defStyle: Int) {
        super.init(animatedTextView, attrs, defStyle)
        animator = ValueAnimator()
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addListener(object : DefaultAnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                mAnimatedTextView.let { getAnimationListener()?.onAnimationEnd(it) }
            }
        })
        animator.addUpdateListener { animation ->
            setProgress(animation.animatedValue as Float)
        }
        mText.length.let { length ->
            val n = if (length <= 0) 1 else length
            duration = (charTime + charTime / mostCount * (n - 1)).toLong()
        }
    }

    override fun animateText(text: CharSequence) {
        mAnimatedTextView.post {
            oldStartX = mAnimatedTextView.layout.getLineLeft(0)
            super@EvaporateText.animateText(text)
        }
    }

    override fun initVariables() {}
    override fun animateStart(text: CharSequence?) {
        mText.length.let { length ->
            val n = if (length <= 0) 1 else length
            duration = (charTime + charTime / mostCount * (n - 1)).toLong()
            animator.apply {
                cancel()
                setFloatValues(0f, 1f)
                duration = duration
                start()
            }
        }
    }

    override fun animatePrepare(text: CharSequence?) {
        differentList.clear()
        differentList.addAll(CharacterUtils.diff(mOldText, mText))
        val bounds = Rect()
        mPaint.getTextBounds(mText.toString(), 0, mText.length, bounds)
        mTextHeight = bounds.height()
    }

    override fun drawFrame(canvas: Canvas?) {
        val startX = mAnimatedTextView.layout.getLineLeft(0)
        val startY = mAnimatedTextView.baseline.toFloat()
        var offset = startX
        var oldOffset = oldStartX
        val maxLength = max(mText.length, mOldText.length)
        for (i in 0 until maxLength) {
            // draw old text
            if (i < mOldText.length) {
                //
                val pp = getProgress() * duration / (charTime + charTime / mostCount
                        * (mText.length - 1))
                mOldPaint.textSize = mTextSize
                val move: Int = CharacterUtils.needMove(i, differentList)
                if (move != -1) {
                    mOldPaint.alpha = 255
                    var p = pp * 2f
                    p = if (p > 1) 1f else p
                    val distX: Float =
                        CharacterUtils.getOffset(i, move, p, startX, oldStartX, gapList, oldGapList)
                    canvas?.drawText(
                        mOldText[i].toString() + "", 0, 1, distX, startY,
                        mOldPaint
                    )
                } else {
                    mOldPaint.alpha = ((1 - pp) * 255).toInt()
                    val y = startY - pp * mTextHeight
                    val width = mOldPaint.measureText(mOldText[i].toString() + "")
                    canvas?.drawText(
                        mOldText[i].toString() + "",
                        0,
                        1,
                        oldOffset + (oldGapList[i] - width) / 2,
                        y,
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
                    alpha = if (alpha > 255) 255 else alpha
                    alpha = if (alpha < 0) 0 else alpha
                    mPaint.alpha = alpha
                    mPaint.textSize = mTextSize
                    val pp = getProgress() * duration / (charTime + charTime / mostCount
                            * (mText.length - 1))
                    val y = mTextHeight + startY - pp * mTextHeight
                    val width = mPaint.measureText(mText[i].toString() + "")
                    canvas?.drawText(
                        mText[i].toString() + "", 0, 1,
                        offset + (gapList[i] - width) / 2, y,
                        mPaint
                    )
                }
                offset += gapList[i]
            }
        }
    }
}