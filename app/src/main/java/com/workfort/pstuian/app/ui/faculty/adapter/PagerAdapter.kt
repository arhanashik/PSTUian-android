package com.workfort.pstuian.app.ui.faculty.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private var fragments: ArrayList<Fragment> = ArrayList()

    fun addItem(fragment: Fragment) {
        fragments.add(fragment)
    }

    fun addItem(fragment: Fragment, position: Int) {
        fragments.add(position, fragment)
    }

    fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    fun removeItem(fragment: Fragment) {
        fragments.remove(fragment)
    }

    fun removeItem(position: Int) {
        fragments.removeAt(position)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}