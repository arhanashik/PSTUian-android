package com.workfort.pstuian.model

import com.workfort.pstuian.util.deeplink.DeepLinkAction

/**
 * Holds parsed deep-link actions from a cold start until splash finishes
 * and navigation can run on the main graph.
 */
class AppLaunchDeepLinkController {

    private var pendingDeepLinkAction: DeepLinkAction? = null

    fun setPendingDeepLink(action: DeepLinkAction) {
        pendingDeepLinkAction = action
    }

    /** Returns null when no deep link was pending. */
    fun consumePendingDeepLink(): DeepLinkAction? {
        val action = pendingDeepLinkAction ?: return null
        pendingDeepLinkAction = null
        return action
    }
}
