package com.workfort.pstuian.app.ui.base

import android.content.SearchRecentSuggestionsProvider
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.R

/**
 *  ****************************************************************************
 *  * Created by : arhan on 02 Oct, 2021 at 3:26 AM.
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

class BaseSuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(
            PstuianApp.getBaseApplicationContext().getString(R.string.search_suggestion_provider_authority), MODE
        )
    }

    companion object {
        const val AUTHORITY = "com.workfort.pstuian.BaseSuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES or DATABASE_MODE_2LINES
    }
}