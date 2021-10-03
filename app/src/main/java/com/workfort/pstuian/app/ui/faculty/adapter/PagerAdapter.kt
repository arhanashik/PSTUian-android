package com.workfort.pstuian.app.ui.faculty.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private var titles: ArrayList<String> = ArrayList()
    private var fragments: ArrayList<Fragment> = ArrayList()

    fun addItem(title: String, fragment: Fragment) {
        titles.add(title)
        fragments.add(fragment)
    }

    fun addItem(title: String, fragment: Fragment, position: Int) {
        titles.add(position, title)
        fragments.add(position, fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    fun removeItem(fragment: Fragment) {
        fragments.remove(fragment)
    }

    fun removeItem(position: Int) {
        fragments.removeAt(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}