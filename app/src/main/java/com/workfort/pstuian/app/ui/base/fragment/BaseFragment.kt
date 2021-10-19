package com.workfort.pstuian.app.ui.base.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.ui.base.callback.BaseLocationCallback
import com.workfort.pstuian.util.helper.NetworkUtil
import com.workfort.pstuian.util.helper.PermissionUtil

/**
 *  ****************************************************************************
 *  * Created by : arhan on 02 Oct, 2021 at 4:33 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/2/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

abstract class BaseFragment<VB : ViewBinding> : Fragment(), View.OnClickListener {
    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    open fun enableLocation(): Boolean {
        return false
    }

    abstract fun afterOnViewCreated(view: View, savedInstanceState: Bundle?)

    open fun onConnectivityChanged(connected: Boolean) { }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLocationService()

        afterOnViewCreated(view, savedInstanceState)

        observeConnectivity()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == Const.RequestCode.LOCATION
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            initLocationService()
        }
    }

    override fun onClick(v: View?) { }

    private fun initLocationService() {
        if(!enableLocation()) return

        activity?.let { activity ->
            if(PermissionUtil.isAllowed(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            } else {
                PermissionUtil.request(
                    this, Const.RequestCode.LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
    }

    fun setClickListener(vararg views: View) {
        views.forEach { view -> view.setOnClickListener(this) }
    }

    /**
     * This function gets user's current location using async FusedLocationClient.
     * If the location permission is not given it requests for the permission.
     * Else it tries to find the current lat and lng.
     * If not found then returns 0.0 as lat and lng.
     *
     * Careful: To use this function one needs to enable the location service from the child fragment
     * by overriding the 'enableLocation()' function
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(callback: BaseLocationCallback) {
        activity?.let { activity ->
            if(PermissionUtil.isAllowed(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
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
    }

    private fun observeConnectivity() {
        context?.let {
            NetworkUtil.from(it).observe(viewLifecycleOwner, { connected ->
                onConnectivityChanged(connected)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}