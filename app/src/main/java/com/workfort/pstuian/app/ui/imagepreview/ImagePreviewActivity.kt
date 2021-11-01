package com.workfort.pstuian.app.ui.imagepreview

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import coil.load
import coil.request.ImageRequest
import coil.request.ImageResult
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.ui.base.activity.BaseActivity
import com.workfort.pstuian.databinding.ActivityImagePreviewBinding

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Nov, 2021 at 12:04.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/11/01.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class ImagePreviewActivity : BaseActivity<ActivityImagePreviewBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityImagePreviewBinding
            = ActivityImagePreviewBinding::inflate

    private var imageUrl: String = ""

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        //get image object from intent extra
        imageUrl = intent.getStringExtra(Const.Key.URL)?: return

        //get shared image view transition name
        intent?.getStringExtra(Const.Key.EXTRA_IMAGE_TRANSITION_NAME)?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.imgPreview.transitionName = it
            }
        }

        //load the image
        loadImage(imageUrl)

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun loadImage(imageUrl: String) {
        binding.imgPreview.load(imageUrl) {
            crossfade(true)
            listener(object : ImageRequest.Listener {
                override fun onSuccess(request: ImageRequest, metadata: ImageResult.Metadata) {
                    //execute postponed shared item transition
                    supportStartPostponedEnterTransition()
                }
            })
        }
    }
}