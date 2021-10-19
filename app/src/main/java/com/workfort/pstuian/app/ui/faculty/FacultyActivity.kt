package com.workfort.pstuian.app.ui.faculty

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.faculty.adapter.PagerAdapter
import com.workfort.pstuian.app.ui.faculty.batch.BatchesFragment
import com.workfort.pstuian.app.ui.faculty.course.CourseFragment
import com.workfort.pstuian.app.ui.faculty.employee.EmployeeFragment
import com.workfort.pstuian.app.ui.faculty.teachers.TeachersFragment
import com.workfort.pstuian.databinding.ActivityFacultyBinding

class FacultyActivity : BaseActivity<ActivityFacultyBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityFacultyBinding
        = ActivityFacultyBinding::inflate

    override fun getToolbarId(): Int = R.id.toolbar
    override fun getMenuId(): Int = R.menu.menu_search
    override fun getSearchMenuItemId(): Int = R.id.action_search

    private lateinit var pagerAdapter: PagerAdapter

    private lateinit var mFaculty: FacultyEntity
    var mSelectedTabPosition = 0

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setHomeEnabled()
        if(savedInstanceState != null) return

        val faculty = intent.getParcelableExtra<FacultyEntity>(Const.Key.FACULTY)
        if(faculty == null) finish()
        else mFaculty = faculty

        title = mFaculty.shortTitle

        initTabs()
    }

    override fun onSearchQueryChange(searchQuery: String) {
        when (mSelectedTabPosition) {
            0 -> (pagerAdapter.getItem(0) as BatchesFragment).filter(searchQuery)
            1 -> (pagerAdapter.getItem(1) as TeachersFragment).filter(searchQuery)
            2 -> (pagerAdapter.getItem(2) as CourseFragment).filter(searchQuery)
            3 -> (pagerAdapter.getItem(3) as EmployeeFragment).filter(searchQuery)
        }
    }

    private fun initTabs() {
        pagerAdapter = PagerAdapter(this)
        pagerAdapter.addItem(BatchesFragment.newInstance(mFaculty))
        pagerAdapter.addItem(TeachersFragment.newInstance(mFaculty))
        pagerAdapter.addItem(CourseFragment.newInstance(mFaculty))
        pagerAdapter.addItem(EmployeeFragment.newInstance(mFaculty))
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = pagerAdapter.itemCount
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mSelectedTabPosition = position
                when(position) {
                    0 -> setSearchQueryHint(getString(R.string.hint_search_batch))
                    1 -> setSearchQueryHint(getString(R.string.hint_search_teacher))
                    2 -> setSearchQueryHint(getString(R.string.hint_search_course_schedule))
                    3 -> setSearchQueryHint(getString(R.string.hint_search_employee))
                    else -> hindSearchView()
                }
            }
        })

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(when(position) {
                0 -> R.string.label_batch
                1 -> R.string.label_teacher
                2 -> R.string.label_course_schedule
                3 -> R.string.label_employee
                else -> R.string.txt_unknown
            })
        }.attach()
    }
}
