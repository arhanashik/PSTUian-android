package com.workfort.pstuian.app.ui.splash

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hanks.htextview.base.AnimationListener
import com.hanks.htextview.base.HTextView
import com.workfort.pstuian.R
import com.workfort.pstuian.databinding.ActivitySplashBinding
import com.workfort.pstuian.app.ui.home.HomeActivity
import timber.log.Timber


class SplashActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashBinding

    private lateinit var mSetLeftIn: AnimatorSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        loadAnimations()
        changeCameraDistance()
        flipCard()
    }

    private fun loadAnimations() {
        mSetLeftIn = AnimatorInflater.loadAnimator(this, R.animator.flip_in) as AnimatorSet
        mSetLeftIn.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationStart(animation: Animator) {
                //mBinding.tvTitle.setAnimationListener { SimpleAnimationListener(this@SplashActivity) }
                mBinding.tvTitle.animateText(getText(R.string.app_name))
                Handler().postDelayed({
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finish()
                }, 2500)
            }
        })
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        mBinding.tvTitle.cameraDistance = scale
    }

    private fun flipCard() {
        mSetLeftIn.setTarget(mBinding.tvTitle)
        mSetLeftIn.start()
    }

    class SimpleAnimationListener(val context: Context) : AnimationListener {
        override fun onAnimationEnd(hTextView: HTextView) {
            Timber.e("working...")
            Toast.makeText(context, "Animation finished", Toast.LENGTH_SHORT).show()
        }
    }
}
