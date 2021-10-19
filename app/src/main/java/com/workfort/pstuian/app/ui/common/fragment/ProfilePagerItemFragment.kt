package com.workfort.pstuian.app.ui.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.workfort.pstuian.app.ui.base.fragment.BaseFragment
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoAdapter
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoClickEvent
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoItem
import com.workfort.pstuian.databinding.FragmentProfilePagerItemBinding

class ProfilePagerItemFragment(
    private val items: List<ProfileInfoItem>,
    private val listener: ProfileInfoClickEvent? = null
) : BaseFragment<FragmentProfilePagerItemBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfilePagerItemBinding
            = FragmentProfilePagerItemBinding::inflate

    companion object {
        fun instance(items: List<ProfileInfoItem>, listener: ProfileInfoClickEvent? = null) =
            ProfilePagerItemFragment(items, listener)
    }

    override fun afterOnViewCreated(view: View, savedInstanceState: Bundle?) {
        initList()

    }

    private fun initList() {
        binding.rvItems.adapter = ProfileInfoAdapter().apply {
            setItems(items.toMutableList())
            if(listener != null) setListener(listener)
        }
    }
}
