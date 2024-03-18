package com.workfort.pstuian.app.ui.faculty.batch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.workfort.pstuian.app.ui.base.fragment.BaseFragment
import com.workfort.pstuian.app.ui.faculty.adapter.BatchesAdapter
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.listener.BatchClickEvent
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.students.StudentsActivity
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.databinding.FragmentBatchesBinding
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.RequestState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class BatchesFragment(private val faculty: FacultyEntity)
    : BaseFragment<FragmentBatchesBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBatchesBinding
            = FragmentBatchesBinding::inflate

    companion object {
        fun newInstance(faculty: FacultyEntity) = BatchesFragment(faculty)
    }

    private val mViewModel: FacultyViewModel by viewModel()
    private lateinit var mAdapter: BatchesAdapter

    override fun afterOnViewCreated(view: View, savedInstanceState: Bundle?) {
        initBatchList()
        observeBatches()
        mViewModel.facultyId = faculty.id
        loadData()
        with(binding) {
            srlReloadData.setOnRefreshListener { loadData(true) }
            btnRefresh.setOnClickListener { loadData(true) }
        }
    }

    private fun initBatchList() {
        mAdapter = BatchesAdapter()
        mAdapter.setListener(object: BatchClickEvent {
            override fun onClickBatch(batch: BatchEntity) {
                val intent = Intent(context, StudentsActivity::class.java)
                intent.putExtra(Const.Key.FACULTY, faculty)
                intent.putExtra(Const.Key.BATCH, batch)
                startActivity(intent)
            }
        })
        binding.rvData.adapter = mAdapter
    }

    private fun loadData(forceRefresh: Boolean = false) {
        mViewModel.batchesStateForceRefresh = forceRefresh
        lifecycleScope.launch {
            mViewModel.intent.send(FacultyIntent.GetBatches)
        }
    }

    private fun observeBatches() {
        lifecycleScope.launch {
            mViewModel.batchesState.collect {
                when (it) {
                    is RequestState.Idle -> Unit
                    is RequestState.Loading -> setActionUiState(true)
                    is RequestState.Success<*> -> {
                        setActionUiState(false)
                        val batches = it.data as List<BatchEntity>
                        renderBatches(batches)
                    }
                    is RequestState.Error -> {
                        setActionUiState(false)
                        renderBatches(emptyList())
                        binding.tvMessage.text = it.error?: "Can't load data"
                    }
                }
            }
        }
    }

    private fun setActionUiState(isLoading: Boolean) {
        val visibility = if(isLoading) View.VISIBLE else View.GONE
        val inverseVisibility = if(isLoading) View.GONE else View.VISIBLE
        with(binding) {
            srlReloadData.isRefreshing = isLoading
            shimmerLayout.visibility = visibility
            if(isLoading) shimmerLayout.startShimmer()
            else shimmerLayout.stopShimmer()

            rvData.visibility = inverseVisibility
            lavError.visibility = inverseVisibility
            tvMessage.visibility = inverseVisibility
            btnRefresh.visibility = inverseVisibility
        }
    }

    private fun renderBatches(data: List<BatchEntity>) {
        val visibility = if(data.isEmpty()) View.GONE else View.VISIBLE
        val inverseVisibility = if(data.isEmpty()) View.VISIBLE else View.GONE
        with(binding) {
            rvData.visibility = visibility
            srlReloadData.visibility = visibility
            lavError.visibility = inverseVisibility
            if(data.isEmpty()) lavError.playAnimation()
            tvMessage.visibility = inverseVisibility
            btnRefresh.visibility = inverseVisibility
        }
        mAdapter.setData(data.toMutableList())
    }

    fun filter(query: String) {
        mAdapter.filter.filter(query)
    }
}
