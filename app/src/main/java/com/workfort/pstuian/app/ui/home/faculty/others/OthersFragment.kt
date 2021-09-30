package com.workfort.pstuian.app.ui.home.faculty.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.workfort.pstuian.R
import com.workfort.pstuian.databinding.FragmentOthersBinding

class OthersFragment : Fragment() {

    companion object {
        fun newInstance() = OthersFragment()
    }

    private lateinit var mBinding: FragmentOthersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_others, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
