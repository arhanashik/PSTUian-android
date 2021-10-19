package com.workfort.pstuian.app.ui.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.databinding.ActivityForgotPasswordBinding
import timber.log.Timber

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityForgotPasswordBinding
            = ActivityForgotPasswordBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()

        with(binding) {
            setClickListener(btnSend, btnSignIn)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v) {
            binding.btnSend -> {

            }
            binding.btnSignIn -> {
                finish()
            }
            else -> {
                Timber.e("Who clicked me!")
            }
        }
    }
}
