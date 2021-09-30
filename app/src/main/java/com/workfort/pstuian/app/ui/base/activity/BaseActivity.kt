package com.workfort.pstuian.app.ui.base.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.ui.base.callback.BaseLocationCallback
import com.workfort.pstuian.util.helper.NetworkUtil
import com.workfort.pstuian.util.helper.PermissionUtil

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 10:36 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

abstract class BaseActivity<VB : ViewBinding>: AppCompatActivity(), View.OnClickListener {
    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var mMenu: Menu

    companion object {
        const val NO_TOOLBAR = -1
        const val NO_MENU = -1
        const val NO_MENU_ITEM = -1
        const val DEFAULT_THEME = -1
    }

    open fun getThemeId() = DEFAULT_THEME

    open fun getToolbarId() = NO_TOOLBAR

    open fun getMenuId() = NO_MENU

    /**
     * If override from child activity and searchMenuItemId is provided
     * the default searchView with search suggestions will be activated.
     * If NO_MENU_ITEM is provided the search option will be skipped
     *
     * To enable default search option also add the SEARCH intent filter
     * and searchable xml config in Manifest. The child activity should
     * have android:launchMode="singleTop"
     *
     * */
    open fun getSearchMenuItemId(): Int {
        return NO_MENU_ITEM
    }

    open fun enableLocation(): Boolean {
        return false
    }

    abstract fun afterOnCreate(savedInstanceState: Bundle?)

    open fun afterOnCreateOptionsMenu(menu: Menu?) {

    }

    /**
     * If default searchView is activated(by providing getSearchMenuItemId()) from
     * child activity the search query provided by te users will be returned in
     * this method. Overriding this method with super call will save the query
     * to show suggestions. The suggestions will be cleared while the activity
     * is destroyed. To prevent this or modifying the suggestion behaviour
     * remove 'clearSearchQueryHistory()' this line from onDestroy() and set as
     * required
     *
     * */
//    open fun onRequestSearch(searchQuery: String?) {
//        if(!TextUtils.isEmpty(searchQuery)) {
//            SearchRecentSuggestions(
//                this, getString(R.string.search_suggestion_provider_authority),
//                BaseSuggestionProvider.MODE
//            ).saveRecentQuery(searchQuery, null)
//        }
//    }

    open fun onConnectivityChanged(connected: Boolean) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        changeTheme()

        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)

        initLocationService()

        setToolbar()

        afterOnCreate(savedInstanceState)

        observeConnectivity()
    }

    override fun onResume() {
        super.onResume()

        /**
         * Sometimes if the app is in onPause() for long time
         * the network status returns wrong state.
         * So, onResume() just refresh the state
         */
        NetworkUtil.refresh()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        handleIntent(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == Const.RequestCode.LOCATION
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            initLocationService()
        }
    }

    override fun onClick(v: View?) { }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if(getMenuId() == NO_MENU) return super.onCreateOptionsMenu(menu)

        menuInflater.inflate(getMenuId(), menu)

        if(menu != null) mMenu = menu
        setSearchEnabled()

        afterOnCreateOptionsMenu(menu)

        return true
    }

    private fun changeTheme() {
        if(getThemeId() == DEFAULT_THEME) return

        setTheme(getThemeId())
    }

    private fun initLocationService() {
        if(!enableLocation()) return

        if(PermissionUtil.isAllowed(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        } else {
            PermissionUtil.request(
                this, Const.RequestCode.LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    private fun setToolbar() {
        if(getToolbarId() == NO_TOOLBAR) return

        val toolbar = findViewById<Toolbar>(getToolbarId())
        if(toolbar != null) setSupportActionBar(toolbar)
    }

    fun setHomeEnabled() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setSearchEnabled() {
        if(!::mMenu.isInitialized || getSearchMenuItemId() == NO_MENU_ITEM) return

        val searchMenuItem = mMenu.findItem(getSearchMenuItemId())
        if(searchMenuItem != null) {
//            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//            (searchMenuItem.actionView as SearchView).apply {
//                setSearchableInfo(searchManager.getSearchableInfo(componentName))
//            }
        }
    }

    fun showMenu(show: Boolean) {
        if(!::mMenu.isInitialized) return

//        mMenu.forEach { item -> item.isVisible = show }
    }

    fun setClickListener(vararg views: View) {
        views.forEach { view -> view.setOnClickListener(this) }
    }

    private fun handleIntent(intent: Intent?) {
//        if (intent?.action == Intent.ACTION_SEARCH) {
//            val searchQuery = intent.getStringExtra(SearchManager.QUERY)
//
//            onRequestSearch(searchQuery)
//        }
    }

    fun saveSearQuery(query: String) {
//        SearchRecentSuggestions(
//            this, getString(R.string.search_suggestion_provider_authority),
//            BaseSuggestionProvider.MODE
//        ).saveRecentQuery(query, null)
    }

    fun clearSearchQueryHistory() {
//        SearchRecentSuggestions(
//            this, getString(R.string.search_suggestion_provider_authority),
//            BaseSuggestionProvider.MODE
//        ).clearHistory()
    }

    private fun observeConnectivity() {
        NetworkUtil.from(this).observe(this, { connected ->
            onConnectivityChanged(connected)
        })
    }

    /*
     * This function gets user's current location using async FusedLocationClient.
     * If the location permission is not given it requests for the permission.
     * Else it tries to find the current lat and lng.
     * If not found then returns 0.0 as lat and lng.
     *
     * Careful: To use this function one needs to enable the location service from the child activity
     * by overriding the 'enableLocation()' function
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(callback: BaseLocationCallback) {

        if(PermissionUtil.isAllowed(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mFusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                callback.onComplete(location?.latitude?: 0.0, location?.longitude?: 0.0)
            }.addOnFailureListener {
                callback.onComplete(0.0, 0.0)
            }
        } else {
            PermissionUtil.request(
                this, Const.RequestCode.LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        clearSearchQueryHistory()
        NetworkUtil.unregister(this)
    }
}