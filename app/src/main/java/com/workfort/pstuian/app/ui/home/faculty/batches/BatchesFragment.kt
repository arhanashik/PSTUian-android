package com.workfort.pstuian.app.ui.home.faculty.batches

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.databinding.FragmentBatchesBinding
import com.workfort.pstuian.app.ui.home.faculty.FacultyActivity
import com.workfort.pstuian.app.ui.home.faculty.adapter.BatchesAdapter
import com.workfort.pstuian.app.ui.home.faculty.batches.students.StudentsActivity
import com.workfort.pstuian.app.ui.home.faculty.listener.BatchClickEvent
import com.workfort.pstuian.util.remote.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class BatchesFragment : Fragment() {

    companion object {
        fun newInstance() = BatchesFragment()
    }

    private lateinit var mBinding: FragmentBatchesBinding
    private lateinit var mViewModel: BatchesViewModel
    private lateinit var mAdapter: BatchesAdapter
    private lateinit var mFaculty: FacultyEntity

    private var mDisposable = CompositeDisposable()
    private val apiService by lazy {
        ApiClient.create()
    }
    private var mTriggeredLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_batches, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mFaculty = (activity as FacultyActivity).mFaculty!!

        mViewModel = ViewModelProviders.of(this).get(BatchesViewModel::class.java)

        mAdapter = BatchesAdapter()
        mAdapter.setListener(object: BatchClickEvent {
            override fun onClickBatch(batch: BatchEntity) {
                val intent = Intent(context, StudentsActivity::class.java)
                intent.putExtra(Const.Key.BATCH, batch)
                startActivity(intent)
            }
        })

        mBinding.rvBatches.layoutManager = LinearLayoutManager(context)
        mBinding.rvBatches.adapter = mAdapter

        mViewModel.getBatches(mFaculty.shortTitle!!).observe(this, Observer {
            if(it.isEmpty()) {
                loadBatches(true)
            }else {
                mAdapter.setBatches(it.toMutableList())

                if(!mTriggeredLoading) {
                    mTriggeredLoading = true
                    loadBatches(false)
                }
            }
        })
    }

    fun filter(query: String) {
        mAdapter.filter.filter(query)
    }

    private fun loadBatches(loadFresh: Boolean) {
        if(loadFresh) {
            mBinding.loader.visibility = View.VISIBLE
            mBinding.rvBatches.visibility = View.INVISIBLE
            mBinding.tvMessage.visibility = View.INVISIBLE
        }

        mDisposable.add(apiService.loadBatches(mFaculty.shortTitle!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(loadFresh) mBinding.loader.visibility = View.INVISIBLE
                if(it.success) {
                    if(it.batches.isNotEmpty()) {
                        if(loadFresh) mBinding.rvBatches.visibility = View.VISIBLE
                        mViewModel.insertBatches(it.batches)
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
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        mDisposable.dispose()
        super.onDestroy()
    }
}
