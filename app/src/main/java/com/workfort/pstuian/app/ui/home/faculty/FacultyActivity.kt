package com.workfort.pstuian.app.ui.home.faculty

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.app.ui.home.faculty.adapter.PagerAdapter
import com.workfort.pstuian.app.ui.home.faculty.batches.BatchesFragment
import com.workfort.pstuian.app.ui.home.faculty.courseschedule.CourseScheduleFragment
import com.workfort.pstuian.app.ui.home.faculty.employee.EmployeeFragment
import com.workfort.pstuian.app.ui.home.faculty.teachers.TeachersFragment
import com.workfort.pstuian.databinding.ActivityFacultyBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


class FacultyActivity : BaseActivity<ActivityFacultyBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityFacultyBinding
        = ActivityFacultyBinding::inflate

    private var searchMenuItem: MenuItem? = null
    private var searchView: SearchView? = null
    private lateinit var pagerAdapter: PagerAdapter

    var mFaculty: FacultyEntity? = null
    private val mDisposable = CompositeDisposable()

    val mSubject = PublishSubject.create<String>()
    var selectedPosition = 0

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) return

        mFaculty = intent.getParcelableExtra(Const.Key.FACULTY)
        if(mFaculty == null) finish()

        setHomeEnabled()
        title = mFaculty?.shortTitle

        initTabs()

        mDisposable.add(mSubject
            //.debounce(300, TimeUnit.MILLISECONDS)
            //.filter { !it.isEmpty() }
            //.distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                when (selectedPosition) {
                    0 -> (pagerAdapter.getItem(0) as BatchesFragment).filter(it)
                    1 -> (pagerAdapter.getItem(1) as TeachersFragment).filter(it)
                    2 -> (pagerAdapter.getItem(2) as CourseScheduleFragment).filter(it)
                    3 -> (pagerAdapter.getItem(3) as EmployeeFragment).filter(it)
                    else -> {

                    }
                }
            }, {
                Timber.e(it)
            })
        )
    }

    private fun initTabs() {
        pagerAdapter = PagerAdapter(supportFragmentManager)
        pagerAdapter.addItem(getString(R.string.label_batch), BatchesFragment.newInstance())
        pagerAdapter.addItem(getString(R.string.label_teacher), TeachersFragment.newInstance())
        pagerAdapter.addItem(getString(R.string.label_course_schedule), CourseScheduleFragment.newInstance())
        pagerAdapter.addItem(getString(R.string.label_employee), EmployeeFragment.newInstance())
        //pagerAdapter.addItem(getString(R.string.label_other), OthersFragment.newInstance())

        binding.viewPager.adapter = pagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        binding.viewPager.offscreenPageLimit = pagerAdapter.count

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                selectedPosition = position
                mSubject.onNext(searchView?.query.toString())
                when(position) {
                    0 -> {
                        searchMenuItem?.isVisible = true
                        searchView?.visibility = View.VISIBLE
                        searchView?.queryHint = getString(R.string.hint_search_teacher)
                    }
                    1 -> {
                        searchMenuItem?.isVisible = true
                        searchView?.visibility = View.VISIBLE
                        searchView?.queryHint = getString(R.string.hint_search_batch)
                    }
                    2 -> {
                        searchMenuItem?.isVisible = true
                        searchView?.visibility = View.VISIBLE
                        searchView?.queryHint = getString(R.string.hint_search_course_schedule)
                    }
                    3 -> {
                        searchMenuItem?.isVisible = true
                        searchView?.visibility = View.VISIBLE
                        searchView?.queryHint = getString(R.string.hint_search_employee)
                    }
                    else -> {
                        searchMenuItem?.isVisible = false
                        searchView?.visibility = View.INVISIBLE
                        searchMenuItem?.collapseActionView()
                    }
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchMenuItem = menu?.findItem(R.id.action_search)
        if (searchMenuItem != null) {
            searchView = searchMenuItem?.actionView as SearchView
        }
        if (searchView != null) {
            searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (!searchView!!.isIconified) {
                        searchView!!.isIconified = true
                    }

                    //mSubject.onComplete()
                    searchMenuItem?.collapseActionView()
                    return true
                }

                override fun onQueryTextChange(s: String): Boolean {
                    mSubject.onNext(s)
                    return false
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        mDisposable.dispose()
        super.onDestroy()
    }
}
