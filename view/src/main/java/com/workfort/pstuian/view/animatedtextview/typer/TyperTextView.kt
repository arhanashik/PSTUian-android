package com.workfort.pstuian.view.animatedtextview.typer

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import com.workfort.pstuian.view.R
import com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView
import com.workfort.pstuian.view.animatedtextview.base.AnimationListener
import java.util.Random

class TyperTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AnimatedTextView(context, attrs, defStyleAttr) {
    private val random: Random
    private var mText: CharSequence
    private lateinit var mHandler: Handler
    private var charIncrease: Int
    private var typerSpeed: Int
    private var animationListener: AnimationListener? = null

    companion object {
        const val INVALIDATE = 0x767
    }

    init {
        val typedArray: TypedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.TyperTextView)
        typerSpeed = typedArray.getInt(R.styleable.TyperTextView_typerSpeed, 100)
        charIncrease = typedArray.getInt(R.styleable.TyperTextView_charIncrease, 2)
        typedArray.recycle()
        random = Random()
        mText = text
        mHandler = Handler(Handler.Callback {
            val currentLength: Int = text.length
            if (currentLength < mText.length) {
                if (currentLength + charIncrease > mText.length) {
                    charIncrease = mText.length - currentLength
                }
                append(mText.subSequence(currentLength, currentLength + charIncrease))
                val randomTime = (typerSpeed + random.nextInt(typerSpeed)).toLong()
                val message = Message.obtain()
                message.what = INVALIDATE
                mHandler.sendMessageDelayed(message, randomTime)
                return@Callback false
            } else {
                animationListener?.onAnimationEnd(this@TyperTextView)
            }
            false
        })
    }

    override fun setAnimationListener(listener: AnimationListener) {
        animationListener = listener
    }

    fun getTyperSpeed(): Int {
        return typerSpeed
    }

    fun setTyperSpeed(typerSpeed: Int) {
        this.typerSpeed = typerSpeed
    }

    fun getCharIncrease(): Int {
        return charIncrease
    }

    fun setCharIncrease(charIncrease: Int) {
        this.charIncrease = charIncrease
    }

    override fun setProgress(progress: Float) {
        text = mText.subSequence(0, (mText.length * progress).toInt())
    }

    override fun animateText(text: CharSequence) {
        mText = text
        setText("")
        val message = Message.obtain()
        message.what = INVALIDATE
        mHandler.sendMessage(message)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeMessages(INVALIDATE)
    }
}