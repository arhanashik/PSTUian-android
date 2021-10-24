package com.workfort.pstuian.app.ui.base.activity

import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.just.agentweb.*
import com.just.agentweb.DefaultWebClient.OpenOtherPageWays
import timber.log.Timber


/**
 *  ****************************************************************************
 *  * Created by : arhan on 24 Oct, 2021 at 18:44.
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

abstract class BaseAgentWebActivity : AppCompatActivity() {
    private var mAgentWeb: AgentWeb? = null
    private var mErrorLayoutEntity: ErrorLayoutEntity? = null
    private var mMiddleWareWebChrome: MiddlewareWebChromeBase? = null
    private var mMiddleWareWebClient: MiddlewareWebClientBase? = null

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        buildAgentWeb()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        buildAgentWeb()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        buildAgentWeb()
    }

    open fun buildAgentWeb() {
        val mErrorLayoutEntity = getErrorLayoutEntity()
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(getAgentWebParent(), ViewGroup.LayoutParams(-1, -1))
            .useDefaultIndicator(getIndicatorColor(), getIndicatorHeight())
            .setWebChromeClient(getWebChromeClient())
            .setWebViewClient(getWebViewClient())
            .setWebView(getWebView())
            .setPermissionInterceptor(getPermissionInterceptor())
            .setWebLayout(getWebLayout())
            .setAgentWebUIController(getAgentWebUIController())
            .interceptUnkownUrl()
            .setOpenOtherPageWays(getOpenOtherAppWay())
            .useMiddlewareWebChrome(getMiddleWareWebChrome())
            .useMiddlewareWebClient(getMiddleWareWebClient())
            .setAgentWebWebSettings(getAgentWebSettings())
            .setMainFrameErrorView(mErrorLayoutEntity.mLayoutRes,
                mErrorLayoutEntity.mReloadResId)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .createAgentWeb()
            .ready()
            .go(getUrl())
    }

    open fun getErrorLayoutEntity(): ErrorLayoutEntity {
        return mErrorLayoutEntity?: ErrorLayoutEntity()
    }

    open fun getAgentWeb(): AgentWeb? = mAgentWeb

    open class ErrorLayoutEntity {
        var mLayoutRes = R.layout.agentweb_error_page
            set(value) { field = if(value <= 0) -1 else value }
        var mReloadResId = 0
            set(value) { field = if(value <= 0) -1 else value }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (mAgentWeb != null && mAgentWeb!!.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        if (mAgentWeb != null) {
            mAgentWeb!!.webLifeCycle.onDestroy()
        }
        super.onDestroy()
    }

    @Nullable
    open fun getUrl(): String? = null

    @Nullable
    fun getAgentWebSettings(): IAgentWebSettings<*> {
        return AgentWebSettingsImpl.getInstance()
    }

    protected abstract fun getAgentWebParent(): ViewGroup
    @Nullable
    protected fun getWebChromeClient(): WebChromeClient? {
        return null
    }

    @ColorInt
    open fun getIndicatorColor(): Int = -1

    open fun getIndicatorHeight(): Int = -1

    @Nullable
    protected fun getWebViewClient(): WebViewClient? = null

    @Nullable
    protected fun getWebView(): WebView? = null

    @Nullable
    protected fun getWebLayout(): IWebLayout<*, *>? = null

    @Nullable
    protected fun getPermissionInterceptor(): PermissionInterceptor? = null

    @Nullable
    fun getAgentWebUIController(): AgentWebUIControllerImplBase? = null

    @Nullable
    fun getOpenOtherAppWay(): OpenOtherPageWays? = null

    open fun getMiddleWareWebChrome(): MiddlewareWebChromeBase {
        return object : MiddlewareWebChromeBase() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                setTitle(view, title)
            }
        }.also { mMiddleWareWebChrome = it }
    }

    open fun setTitle(view: WebView?, title: String?) = Unit

    open fun getMiddleWareWebClient(): MiddlewareWebClientBase {
        return object : MiddlewareWebClientBase() {
            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                onHttpError(view, request, errorResponse)
            }
        }.also { mMiddleWareWebClient = it }
    }

    open fun goPreviousOrFinish() {
        mAgentWeb?.let { agentWeb ->
            if(!agentWeb.back()) {
                finish()
            }
        }
    }

    open fun onHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) = Unit
}