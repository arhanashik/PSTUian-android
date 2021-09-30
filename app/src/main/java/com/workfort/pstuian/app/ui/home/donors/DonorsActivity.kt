package com.workfort.pstuian.app.ui.home.donors

import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.donor.DonorEntity
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.databinding.ActivityDonorsBinding
import com.workfort.pstuian.databinding.PromptDonateBinding
import com.workfort.pstuian.databinding.PromptDonationMessageBinding
import com.workfort.pstuian.app.ui.home.faculty.adapter.DonorsAdapter
import com.workfort.pstuian.app.ui.home.faculty.listener.DonorClickEvent
import com.workfort.pstuian.util.helper.NetworkUtil
import com.workfort.pstuian.util.remote.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class DonorsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDonorsBinding
    private lateinit var mAdapter: DonorsAdapter

    private var mDisposable = CompositeDisposable()
    private val apiService by lazy {
        ApiClient.create()
    }

    val mSubject = PublishSubject.create<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_donors)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAdapter = DonorsAdapter()
        mAdapter.setListener(object: DonorClickEvent {
            override fun onClickDonor(donor: DonorEntity) {

            }
        })

        mBinding.rvDonors.layoutManager = LinearLayoutManager(this)
        mBinding.rvDonors.adapter = mAdapter

        mDisposable.add(mSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                mAdapter.filter.filter(it)
            }, {
                Timber.e(it)
            })
        )

        loadDonors()

        mBinding.tvFirstDonor.setOnClickListener { donate() }
        mBinding.fabDonate.setOnClickListener { donate() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchMenuItem = menu?.findItem(R.id.action_search)
        if (searchMenuItem != null) {
            val searchView = searchMenuItem.actionView as SearchView
            searchView.queryHint = getString(R.string.hint_search_donors)
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

    private fun loadDonors() {
        mBinding.loader.visibility = View.VISIBLE
        mBinding.rvDonors.visibility = View.INVISIBLE
        mBinding.tvMessage.visibility = View.INVISIBLE
        mBinding.tvFirstDonor.visibility = View.INVISIBLE

        mDisposable.add(apiService.loadDonors()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mBinding.loader.visibility = View.INVISIBLE
                if(it.success) {
                    if(it.donors.isNotEmpty()) {
                        mBinding.rvDonors.visibility = View.VISIBLE
                        mAdapter.setDonors(it.donors)
                    }else{
                        mBinding.tvMessage.visibility = View.VISIBLE
                        mBinding.tvFirstDonor.visibility = View.VISIBLE
                    }
                }else {
                    mBinding.tvMessage.visibility = View.VISIBLE
                    mBinding.tvFirstDonor.visibility = View.VISIBLE
                }
            }, {
                Timber.e(it)
                mBinding.loader.visibility = View.INVISIBLE
                mBinding.tvMessage.visibility = View.VISIBLE
                mBinding.tvFirstDonor.visibility = View.VISIBLE
                showToast(it.message.toString())
            })
        )
    }

    private fun donate() {
        val donationMessage = DataBindingUtil.inflate<PromptDonationMessageBinding>(
            layoutInflater, R.layout.prompt_donation_message, null, false)

        val alertDialog = AlertDialog.Builder(this)
            .setView(donationMessage.root)
            .create()

        donationMessage.btnDonate.setOnClickListener {
            alertDialog.dismiss()
            showDonationOption()
        }

        alertDialog.show()
    }

    private fun showDonationOption() {
        val donationView = DataBindingUtil.inflate<PromptDonateBinding>(
            layoutInflater, R.layout.prompt_donate, null, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            donationView.tvDonationInfo.text = Html.fromHtml(
                Prefs.donateOption,
                Html.FROM_HTML_MODE_LEGACY)
        } else {
            donationView.tvDonationInfo.text = Html.fromHtml(Prefs.donateOption)
        }

        donationView.btnSaveDonation.setOnClickListener { }

        val alertDialog = AlertDialog.Builder(this)
            .setView(donationView.root)
            .create()

        donationView.btnSaveDonation.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable()) {
                val name = donationView.donationName.text.toString()
                val info = donationView.donationInfo.text.toString()
                val email = donationView.donationEmail.text.toString()
                val reference = donationView.donationReference.text.toString()

                saveDonation(name, info, email, reference, alertDialog)
            }else {
                showToast(getString(R.string.internet_not_available_exception))
            }
        }

        alertDialog.show()
    }

    private fun saveDonation(name: String, info: String, email: String, reference: String,
                             dialog: AlertDialog) {
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(info) || TextUtils.isEmpty(email)
            || TextUtils.isEmpty(reference)) {
            showToast(getString(R.string.required_field_missing_exception))
            return
        }else {
            showToast(getString(R.string.saving_donation_message))
        }

        mDisposable.add(
            apiService.saveDonation(name, info, email, reference)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dialog.dismiss()
                    showToast(it.message)
                    if (it.success) {
                        Prefs.donationId = it.donationId
                    }
                }, {
                    Timber.e(it)
                    dialog.dismiss()
                    showToast(it.message.toString())
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
