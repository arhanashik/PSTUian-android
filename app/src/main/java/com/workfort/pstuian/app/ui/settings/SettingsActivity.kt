package com.workfort.pstuian.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.donate.DonateActivity
import com.workfort.pstuian.databinding.ActivitySettingsBinding
import com.workfort.pstuian.util.extension.launchActivity
import com.workfort.pstuian.util.helper.LinkUtil

class SettingsActivity: BaseActivity<ActivitySettingsBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivitySettingsBinding
            = ActivitySettingsBinding::inflate

    override fun getToolbarId() = R.id.toolbar

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()

        val linkUtil = LinkUtil(this@SettingsActivity)
        with(binding.content) {
            btnDonate.setOnClickListener { launchActivity<DonateActivity>() }
            btnContactUs.setOnClickListener {
                linkUtil.sendEmail(getString(R.string.dev_team_email))
            }
        }
    }
}
