package com.workfort.pstuian.app.ui.webview

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.ui.base.activity.BaseAgentWebActivity
import com.workfort.pstuian.databinding.ActivityWebViewBinding
import com.workfort.pstuian.util.helper.LinkUtil


/**
 *  ****************************************************************************
 *  * Created by : arhan on 24 Oct, 2021 at 18:42.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/24.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class WebViewActivity : BaseAgentWebActivity() {
    private lateinit var binding: ActivityWebViewBinding
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        with(binding) {
            toolbar.title = ""
            toolbar.setNavigationOnClickListener { finish() }

            errorContainer.btnReload.setOnClickListener {
                container.visibility = View.VISIBLE
                errorContainer.root.visibility = View.GONE
                getAgentWeb()?.urlLoader?.reload()
            }

            btnOpenInExternalBrowser.setOnClickListener {
                getUrl()?.let { url ->
                    LinkUtil(this@WebViewActivity).openBrowser(url)
                }
            }
        }
    }

    override fun onBackPressed() {
        goPreviousOrFinish()
    }

    override fun getAgentWebParent(): ViewGroup = binding.container

    override fun getIndicatorColor(): Int = ContextCompat.getColor(this,
        R.color.colorPrimary)

    override fun setTitle(view: WebView?, title: String?) {
        super.setTitle(view, title)
        binding.toolbar.title = if (title.isNullOrEmpty()) "" else title
    }

    override fun getIndicatorHeight(): Int = 3

    override fun getUrl() = intent.getStringExtra(Const.Key.URL)

    override fun getErrorLayoutEntity(): ErrorLayoutEntity {
        val entity =  ErrorLayoutEntity()
        entity.mLayoutRes = R.layout.layout_web_error
        entity.mReloadResId = R.id.btn_reload
        return entity
    }

    override fun onHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onHttpError(view, request, errorResponse)
        val statusCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            errorResponse?.statusCode
        } else -1

        val errorMessage = getString(when(statusCode) {
            404 -> R.string.msg_404_error
            else -> R.string.msg_web_error
        })
        with(binding) {
            container.visibility = View.GONE
            errorContainer.root.visibility = View.VISIBLE
            errorContainer.tvMessage.text = errorMessage
        }
    }
}