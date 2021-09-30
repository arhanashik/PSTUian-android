package com.workfort.pstuian.app.ui.home.faculty.batches.students

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.databinding.ActivityStudentsBinding
import com.workfort.pstuian.databinding.PromptStudentDetailsBinding
import com.workfort.pstuian.app.ui.home.faculty.adapter.StudentsAdapter
import com.workfort.pstuian.app.ui.home.faculty.listener.StudentClickEvent
import com.workfort.pstuian.util.helper.ImageLoader
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.remote.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class StudentsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityStudentsBinding
    private lateinit var mViewModel: StudentsViewModel
    private lateinit var mAdapter: StudentsAdapter
    private var mBatch: BatchEntity? = null

    private var mDisposable = CompositeDisposable()
    private val apiService by lazy {
        ApiClient.create()
    }
    private var mTriggeredLoading = false

    val mSubject = PublishSubject.create<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBatch = intent.getParcelableExtra(Const.Key.BATCH)
        if (mBatch == null) finish()

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_students)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = mBatch?.title

        mViewModel = ViewModelProviders.of(this).get(StudentsViewModel::class.java)

        mAdapter = StudentsAdapter()
        mAdapter.setListener(object: StudentClickEvent {
            override fun onClickStudent(student: StudentEntity) {
                showStudentDetails(student)
            }
        })

        mBinding.rvStudents.layoutManager = LinearLayoutManager(this)
        mBinding.rvStudents.adapter = mAdapter

        mViewModel.getStudents(mBatch?.faculty!!, mBatch?.name!!).observe(this, Observer {
            if(it.isEmpty()) {
                loadStudents(true)
            }else {
                mAdapter.setStudents(it.toMutableList())

                if(!mTriggeredLoading) {
                    mTriggeredLoading = true
                    loadStudents(false)
                }
            }
        })

        mDisposable.add(mSubject
            //.debounce(300, TimeUnit.MILLISECONDS)
            //.filter { !it.isEmpty() }
            //.distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                mAdapter.filter.filter(it)
            }, {
                Timber.e(it)
            })
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchMenuItem = menu?.findItem(R.id.action_search)
        if (searchMenuItem != null) {
            val searchView = searchMenuItem.actionView as SearchView
            searchView.queryHint = getString(R.string.hint_search_student)
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (!searchView.isIconified) {
                        searchView.isIconified = true
                    }

                    //mSubject.onComplete()
                    searchMenuItem.collapseActionView()
                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {
                    mSubject.onNext(s)
                    return false
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun showStudentDetails(student: StudentEntity) {
        val binding = DataBindingUtil.inflate(layoutInflater,
            R.layout.prompt_student_details, null, false) as PromptStudentDetailsBinding

        ImageLoader.load(binding.imgProfile, student.imageUrl)
        binding.tvName.text = student.name
        val batchDetails = student.faculty + " " + student.batch
        binding.tvBatchFaculty.text = batchDetails
        val id = "Id: " + student.id
        binding.tvId.text = id
        val reg = "Reg: " + student.reg
        binding.tvReg.text = reg
        val blood = "Blood group: " + student.blood
        binding.tvBlood.text = blood
        val address = "Address: " + student.address
        binding.tvAddress.text = address
        val phone = "Phone: " + student.phone
        binding.tvPhone.text = phone
        val email = "Email: " + student.email
        binding.tvEmail.text = email

        val linkUtil = LinkUtil(this)
        binding.btnCall.setOnClickListener { linkUtil.callTo(student.phone!!) }
        binding.btnEmail.setOnClickListener { linkUtil.sendEmail(student.email!!) }
        binding.tvLinkedIn.setOnClickListener { linkUtil.openBrowser(student.linkedIn!!) }
        binding.tvFbLink.setOnClickListener { linkUtil.openBrowser(student.fbLink!!) }

        val detailsDialog = AlertDialog.Builder(this)
            .setView(binding.root).create()

        detailsDialog.show()
    }

    private fun loadStudents(loadFresh: Boolean) {
        if(loadFresh) {
            mBinding.loader.visibility = View.VISIBLE
            mBinding.rvStudents.visibility = View.INVISIBLE
            mBinding.tvMessage.visibility = View.INVISIBLE
        }

        mDisposable.add(apiService.loadStudents(mBatch?.faculty!!, mBatch?.name!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(loadFresh) mBinding.loader.visibility = View.INVISIBLE
                if(it.success) {
                    if(it.students.isNotEmpty()) {
                        if(loadFresh) mBinding.rvStudents.visibility = View.VISIBLE
                        mViewModel.insertStudents(it.students)
                    }else{
                        if(loadFresh) mBinding.tvMessage.visibility = View.VISIBLE
                    }
                }else {
                    if(loadFresh) mBinding.tvMessage.visibility = View.VISIBLE
                }
            }, {
                Timber.e(it)
                if(loadFresh) {
                    mBinding.loader.visibility = View.INVISIBLE
                    mBinding.tvMessage.visibility = View.VISIBLE
                    showToast(it.message.toString())
                }
            })
        )
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        mDisposable.dispose()
        super.onDestroy()
    }
}
