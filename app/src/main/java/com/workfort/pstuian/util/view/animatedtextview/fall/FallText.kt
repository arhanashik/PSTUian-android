package com.workfort.pstuian.util.view.animatedtextview.fall

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.workfort.pstuian.util.view.animatedtextview.base.*
import java.util.*
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 1:00.
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

class FallText : AnimatedText() {
    private var mostCount = 20f
    private var charTime = 400f
    var interpolator = OvershootInterpolator()
    private val differentList: MutableList<CharacterDiffResult> = ArrayList()
    private var duration: Long = 0
    private var animator: ValueAnimator = ValueAnimator()
    private var mTextHeight = 0

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
        mText.length.let { length ->
            val n = if (length <= 0) 1 else length
            duration = (charTime + charTime / mostCount * (n - 1)).toLong()
        }
    }

    override fun animateText(text: CharSequence) {
        mAnimatedTextView.post(Runnable {
            if (mAnimatedTextView.layout == null) {
                return@Runnable
            }
            oldStartX = mAnimatedTextView.layout.getLineLeft(0)
            super@FallText.animateText(text)
        })
    }

    override fun initVariables() {}
    override fun animatePrepare(text: CharSequence?) {
        differentList.clear()
        differentList.addAll(CharacterUtils.diff(mOldText, mText))
        val bounds = Rect()
        mPaint.getTextBounds(mText.toString(), 0, mText.length, bounds)
        mTextHeight = bounds.height()
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
                //
                val percent: Float = getProgress() * duration / (charTime + charTime / mostCount
                        * (mText.length - 1))
                mOldPaint.textSize = mTextSize
                val move: Int = CharacterUtils.needMove(i, differentList)
                if (move != -1) {
                    mOldPaint.alpha = 255
                    var p = percent * 2f
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
                    mOldPaint.alpha = 255
                    val centerX: Float = oldOffset + oldGapList[i] / 2
                    val width: Float = mOldPaint.measureText(mOldText[i].toString() + "")
                    var p = percent * 1.4f
                    p = if (p > 1) 1f else p
                    p = interpolator.getInterpolation(p)
                    var angle = (1 - p) * Math.PI
                    if (i % 2 == 0) {
                        angle = p * Math.PI + Math.PI
                    }
                    val disX = centerX + (width / 2 * cos(angle)).toFloat()
                    val disY = startY + (width / 2 * sin(angle)).toFloat()
                    mOldPaint.style = Paint.Style.STROKE
                    val path = Path()
                    path.moveTo(disX, disY)
                    // (m+x)/2=a ,x=2a-m
                    // (n+y)/2=b ,y=2b-n
                    path.lineTo(2 * centerX - disX, 2 * startY - disY)
                    if (percent <= 0.7) {
                        canvas?.drawTextOnPath(
                            mOldText[i].toString() + "",
                            path,
                            0f,
                            0f,
                            mOldPaint
                        )
                    } else {
                        val p2 = ((percent - 0.7) / 0.3f).toFloat()
                        mOldPaint.alpha = ((1 - p2) * 255).toInt()
                        val y = (p2 * mTextHeight)
                        val path2 = Path()
                        path2.moveTo(disX, disY + y)
                        path2.lineTo(2 * centerX - disX, 2 * startY - disY + y)
                        canvas?.drawTextOnPath(
                            mOldText[i].toString() + "",
                            path2,
                            0f,
                            0f,
                            mOldPaint
                        )
                    }
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
                    var size: Float =
                        mTextSize * 1f / charTime * (getProgress() * duration - charTime * i / mostCount)
                    size = if (size > mTextSize) mTextSize else size
                    size = if (size < 0) 0f else size
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